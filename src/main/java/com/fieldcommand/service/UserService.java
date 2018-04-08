package com.fieldcommand.service;

import com.fieldcommand.model.Role;
import com.fieldcommand.model.User;
import com.fieldcommand.repository.RoleRepository;
import com.fieldcommand.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean registerUser(User user) {

        Role userRole = roleRepository.findByRole("ROLE_NEW");

        if(userRole == null) {
            // TODO
        } else {
            user.addRole(userRole);
        }
        String activationKey = generateKey();
        user.setActivationKey(activationKey);

        try {
            emailService.sendMessage(user.getEmail(), activationKey);
        } catch (MailException ex) {
            logger.error("Failed to send e-mail to {}, reason: {}", user.getEmail(), ex.getMessage());
            return false;
        }
        userRepository.save(user);
        logger.info("A new user has been added: {}, e-mail: {}", user.getUsername(), user.getEmail());
        return true;

    }

    private String generateKey() {
        Random random = new Random();
        char[] word = new char[16];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }
}
