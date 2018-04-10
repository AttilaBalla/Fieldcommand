package com.fieldcommand.service;

import com.fieldcommand.model.Role;
import com.fieldcommand.model.RoleType;
import com.fieldcommand.model.User;
import com.fieldcommand.model.json.GenericResponseJson;
import com.fieldcommand.repository.RoleRepository;
import com.fieldcommand.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.fieldcommand.util.KeyGenerator.*;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }



    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<HashMap<String, String>> findAll() {
        List<User> users = userRepository.findAll();
        System.out.println(users);
        List<HashMap<String, String>> usersMap = new ArrayList<>();

        for (User user: users) {
            usersMap.add(user.convertToMap());
        }

        return usersMap;
    }

    public GenericResponseJson validateInvite(String email, String username, GenericResponseJson response) {

        if(username.equals("") || email.equals("")) {
            response.setSuccess(false);
            response.setInformation("The fields cannot be empty!");

            return response;
        }

        User user = findUserByEmail(email);

        if(user != null) {
            response.setSuccess(false);
            response.setInformation("An account with this e-mail address already exists!");
            return response;
        }

        response.setSuccess(true);

        return response;
    }

    public boolean registerUser(User user) throws RoleNotFoundException, MailException {

        Role userRole = roleRepository.findByRole(RoleType.ROLE_NEW);

        if(userRole == null) {
            throw new RoleNotFoundException("Role does not exist in the database!");
        } else {
            user.addRole(userRole);
        }

        String activationKey = generateKey();
        user.setActivationKey(activationKey);
        emailService.sendMessage(user.getEmail(), activationKey);

        userRepository.save(user);
        logger.info("A new user has been added: {}, e-mail: {}", user.getUsername(), user.getEmail());

        return true;
    }
}
