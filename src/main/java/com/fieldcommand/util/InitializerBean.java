package com.fieldcommand.util;

import com.fieldcommand.model.Role;
import com.fieldcommand.model.RoleType;
import com.fieldcommand.model.User;
import com.fieldcommand.repository.RoleRepository;
import com.fieldcommand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.fieldcommand.util.KeyGenerator.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InitializerBean {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public InitializerBean(RoleRepository roleRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {

        List<Role> roles = new ArrayList<>();

        roles.add(new Role(RoleType.ROLE_DISABLED, 0));
        roles.add(new Role(RoleType.ROLE_NEW, 1));
        roles.add(new Role(RoleType.ROLE_USER, 10));
        roles.add(new Role(RoleType.ROLE_DEVELOPER, 20));
        roles.add(new Role(RoleType.ROLE_ADMIN, 30));

        roleRepository.save(roles);

    }

    private void initializeUsers() {

        List<User> users = new ArrayList<>();

        Role newUser = roleRepository.findByRole(RoleType.ROLE_NEW);
        Role user = roleRepository.findByRole(RoleType.ROLE_USER);
        users.add(new User("user@email1.com", "user1", newUser, generateKey()));
        users.add(new User("user@email2.com", "user2", user, generateKey()));
        users.add(new User("user@email3.com", "user3", newUser, generateKey()));

        User admin = new User("user@email4.com", "user4");
        Set<Role> adminRoles = new HashSet<>();

        adminRoles.add(roleRepository.findByRole(RoleType.ROLE_ADMIN));
        adminRoles.add(roleRepository.findByRole(RoleType.ROLE_DEVELOPER));
        adminRoles.add(roleRepository.findByRole(RoleType.ROLE_USER));

        admin.setRoles(adminRoles);

        userRepository.save(admin);
        userRepository.save(users);
    }
}
