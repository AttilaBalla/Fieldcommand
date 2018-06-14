package com.fieldcommand.user;

import com.fieldcommand.payload.user.UpdateJson;
import com.fieldcommand.role.Role;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.utility.EmailSender;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import java.util.*;


import static com.fieldcommand.utility.KeyGenerator.*;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailSender emailSender;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       EmailSender emailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailSender = emailSender;
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

        public List<HashMap<String, String>> findAll() {
        List<User> users = userRepository.findAll();
        List<HashMap<String, String>> userData = new ArrayList<>();

        for (User user: users) {
            userData.add(user.getSimpleUserDetails());
        }

        return userData;
    }

    public HashMap<String, String> findAllRolesOfAllUsers() {
        List<User> userList = userRepository.findAll();
        HashMap<String, String> roles = new HashMap<>();

        for (User user: userList
             ) {
            roles.put(user.getId().toString(), user.getRoleString());
        }

        return roles;
    }

    public GenericResponseJson validateInvite(String email, String username, GenericResponseJson response) {

        if(username.equals("") || email.equals("")) {
            response.setSuccess(false);
            response.setInformation("The fields cannot be empty!");

            return response;
        }

        if(userRepository.findUserByUsername(username) != null) {
            response.setSuccess(false);
            response.setInformation("An account with this username already exists!");

            return response;
        }

        if(userRepository.findUserByEmail(email ) != null) {
            response.setSuccess(false);
            response.setInformation("An account with this e-mail address already exists!");
            return response;
        }

        response.setSuccess(true);

        return response;
    }

    public boolean registerUser(User user) throws RoleNotFoundException, MailException {

        Role userRole = roleRepository.findByRoleType(RoleType.ROLE_NEW);

        if(userRole == null) {
            throw new RoleNotFoundException("Role does not exist in the database!");
        } else {
            user.setRole(userRole);
        }

        String activationKey = generateKey();
        user.setActivationKey(activationKey);
        emailSender.sendMessage(user.getEmail(), activationKey);

        userRepository.save(user);
        logger.info("A new user has been added: {}, e-mail: {}", user.getUsername(), user.getEmail());

        return true;
    }

    public void activateUser(String key, String password) throws UserNotFoundException {

        User user = userRepository.findUserByActivationKey(key);

        if(user == null) {
            throw new UserNotFoundException("No user belongs to that key!");
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setPassword(passwordHash);
        user.setActivationKey("");
        user.setRole(roleRepository.findByRoleType(RoleType.ROLE_USER));

        userRepository.save(user);
        logger.info("An account has been activated: {}", user.getUsername());

    }

    public void updateUser(UpdateJson updateJson, String updaterName)
            throws UserNotFoundException, IllegalArgumentException, UnauthorizedModificationException {

        Long userId = updateJson.getId();
        String roleString = updateJson.getRole();

        User user = userRepository.findUserById(userId);
        User updater = userRepository.findUserByUsername(updaterName);
        Role role = roleRepository.findByRoleType(RoleType.valueOf(roleString));

        if(user == null) {
            throw new UserNotFoundException("No user exists with this ID!");
        }

        if(updater == null) {
            throw new UserNotFoundException("Could not retrieve information about updating user!");
        }

        if(!validateAuthorizationToUpdate(updater, user, role)) {
            throw new UnauthorizedModificationException("Update is not authorized for this account!");
        }

        if(!validateUniqueNameAndEmail(updateJson)) { // validate unique name & email constraint
            throw new IllegalArgumentException("Username and Email must be unique for every user!");
        } else {
            user.setEmail(updateJson.getEmail());
            user.setUsername(updateJson.getUsername());
            user.setRole(role);

            userRepository.save(user);

        }
    }

    private boolean validateAuthorizationToUpdate(User updater, User user, Role role) {

        if(user.getRole().getRoleType() == RoleType.ROLE_OWNER &&
                role.getRoleType() != RoleType.ROLE_OWNER) {
            return false; // owner cannot demote himself but can change other fields
        }

        if(user == updater) { // user is updating it's own stuff
            return true;
        } else return updater.getRole().getPower() > user.getRole().getPower();

    }

    private boolean validateUniqueNameAndEmail(UpdateJson updateJson) {

        long userId = updateJson.getId();
        String username = updateJson.getUsername();
        String email = updateJson.getEmail();

        User user = userRepository.findUserByEmail(email);

        if(user != null && user.getId() != userId) {
            return false;
        }

        user = userRepository.findUserByUsername(username);

        return user == null || user.getId() == userId;
    }

    @Transactional
    public UserDetails createUserPrincipalByUserId(Long id) {
        User user = userRepository.findUserById(id);

        return UserPrincipal.create(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User could not be found: " + username);
        }

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new UserNotFoundException("User could not be found: " + id);
        }

        return UserPrincipal.create(user);
    }

}
