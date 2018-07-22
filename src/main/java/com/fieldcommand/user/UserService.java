package com.fieldcommand.user;

import com.fieldcommand.payload.user.UpdateJson;
import com.fieldcommand.project.Project;
import com.fieldcommand.project.ProjectRepository;
import com.fieldcommand.role.Role;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.payload.GenericResponseJson;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.utility.EmailSender.EmailSender;
import com.fieldcommand.utility.Exception.UnauthorizedModificationException;
import com.fieldcommand.utility.Exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import java.util.*;


import static com.fieldcommand.utility.KeyGenerator.*;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ProjectRepository projectRepository;
    private EmailSender emailSender;

    private int userCount;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projectRepository = projectRepository;
    }

    @Autowired
    @Qualifier("springMail")
    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userCount = userRepository.countActiveUsers();

        logger.info("usercount: {}", userCount);
    }

    public int getUserCount() {
        return userCount;
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<HashMap<String, Object>> findAll() {
        List<User> users = userRepository.findAll();
        List<HashMap<String, Object>> userData = new ArrayList<>();

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

    public void registerUser(User user) throws RoleNotFoundException, MailSendException {

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
    }

    public void activateUser(String key, String password, String username) throws UserNotFoundException {

        User user = userRepository.findUserByActivationKey(key);

        if(user == null) {
            throw new UserNotFoundException("No user belongs to that key!");
        }
        // if we find a user with that name and it's not this user then that name is taken
        if(userRepository.findUserByUsername(username) != null && !username.equals(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setPassword(passwordHash);
        user.setActivationKey("");
        user.setUsername(username);

        if(user.getRole().getRoleType() == RoleType.ROLE_NEW) {
            user.setRole(roleRepository.findByRoleType(RoleType.ROLE_USER));
        }

        userRepository.save(user);
        userCount = userRepository.countActiveUsers();
        logger.info("An account has been activated: {}", user.getUsername());

    }

    public void prepareUserUpdate(UpdateJson updateJson, String updaterName)
            throws UserNotFoundException, IllegalArgumentException,
            UnauthorizedModificationException, TransactionSystemException {

        User user = userRepository.findUserById(updateJson.getId());
        User updater = userRepository.findUserByUsername(updaterName);
        Role newRole = roleRepository.findByRoleType(RoleType.valueOf(updateJson.getRole()));

        if(user == null) {
            throw new UserNotFoundException("No user exists with this ID!");
        }

        Role oldRole = user.getRole();

        if(updater == null) {
            throw new UserNotFoundException("Could not retrieve information about updating user!");
        }

        if(!validateAuthorizationToUpdate(updater, user, newRole)) {
            throw new UnauthorizedModificationException("Update is not authorized for this account!");
        }

        if(!validateUniqueNameAndEmail(updateJson)) { // validate unique name & email constraint
            throw new IllegalArgumentException("Username and Email must be unique for every user!");
        } else {

            performUserUpdate(user, updateJson, newRole, oldRole);

        }
    }

    private void performUserUpdate(User user, UpdateJson updateJson, Role newRole, Role oldRole) {

        user.setEmail(updateJson.getEmail());
        user.setUsername(updateJson.getUsername());
        user.setRole(newRole);
        user.setProjects(makeProjectsSet(updateJson.getProjects()));

        userRepository.save(user);

        userCount = userRepository.countActiveUsers();
    }

    private boolean validateAuthorizationToUpdate(User updater, User user, Role role) {

        if (user.getRole().getRoleType() == RoleType.ROLE_OWNER &&
                role.getRoleType() != RoleType.ROLE_OWNER) {
            return false; // owner cannot demote himself but can change other fields
        }

        // user is updating it's own stuff
        return user == updater || updater.getRole().getPower() > user.getRole().getPower();

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

    private Set<Project> makeProjectsSet(List<String> projects) {

        Set<Project> projectsSet = new HashSet<>();

        if(projects.isEmpty()) {
            return projectsSet;
        }

        for (String project: projects
                ) {
            projectsSet.add(projectRepository.findByShortName(project));
        }

        return projectsSet;
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
