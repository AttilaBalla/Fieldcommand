package com.fieldcommand.user;

import com.fieldcommand.json.user.UpdateJson;
import com.fieldcommand.role.Role;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.json.GenericResponseJson;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.utility.EmailSender;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.fieldcommand.utility.KeyGenerator.*;

@Service
public class UserService implements UserDetailsService{

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            String message = "Username not found: " + username;
            logger.info(message);
            throw new UsernameNotFoundException(message);
        }

        List<GrantedAuthority> authorities = user.getRole().getRoleType().getAuthorities()
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(user.getRoleString()));

        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                (user.getRole().getRoleType() != RoleType.ROLE_DISABLED), // enabled
                true, //Account Not Expired
                true, //Credentials Not Expired
                true,//Account Not Locked
                authorities
        ) {
        };

    }

    public void updateUser(UpdateJson updateJson) throws UserNotFoundException, IllegalArgumentException {

        Long userId = updateJson.getId();
        String roleString = updateJson.getRole();

        User user = userRepository.findUserById(userId);
        // Will throw IllegalArgument if not exact match
        Role role = roleRepository.findByRoleType(RoleType.valueOf(roleString));

        if(user == null) {
            throw new UserNotFoundException("No user exists with this ID!");
        }

        user.setEmail(updateJson.getEmail());
        user.setUsername(updateJson.getUsername());
        user.setRole(role);

        userRepository.save(user);

    }
}
