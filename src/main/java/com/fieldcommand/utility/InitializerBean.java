package com.fieldcommand.utility;

import com.fieldcommand.newsfeed.NewsPost;
import com.fieldcommand.newsfeed.NewsPostRepository;
import com.fieldcommand.role.Role;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.user.User;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.fieldcommand.utility.KeyGenerator.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitializerBean {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private NewsPostRepository newsPostRepository;

    private static User ownerUser;

    @Autowired
    public InitializerBean(RoleRepository roleRepository,
                           UserRepository userRepository,
                           NewsPostRepository newsPostRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.newsPostRepository = newsPostRepository;
        initializeRoles();
        initializeUsers();
        initializeNews();
    }

    private void initializeRoles() {

        List<Role> roles = new ArrayList<>();

        roles.add(new Role(RoleType.ROLE_DISABLED, 0));
        roles.add(new Role(RoleType.ROLE_NEW, 1));
        roles.add(new Role(RoleType.ROLE_USER, 10));
        roles.add(new Role(RoleType.ROLE_DEVELOPER, 20));
        roles.add(new Role(RoleType.ROLE_ADMIN, 30));
        roles.add(new Role(RoleType.ROLE_OWNER, 40));

        roleRepository.save(roles);

    }

    private void initializeUsers() {

        List<User> users = new ArrayList<>();

        Role newUser = roleRepository.findByRoleType(RoleType.ROLE_NEW);
        Role user = roleRepository.findByRoleType(RoleType.ROLE_USER);
        Role owner = roleRepository.findByRoleType(RoleType.ROLE_OWNER);

        users.add(new User("user@email1.com", "user1", newUser, generateKey()));
        users.add(new User("user@email2.com", "user2", user, generateKey()));
        users.add(new User("user@email3.com", "user3", newUser, generateKey()));

        ownerUser = new User("user@email4.com", "XAttus", owner);
        ownerUser.setPassword("$2a$10$9fQS0odOowHrEnZcpO93s.00RPWfdVrpoVpaSl3LpDE.z7RuxjVF6");
        users.add(ownerUser);

        userRepository.save(users);
    }

    private void initializeNews() {

        newsPostRepository.save(new NewsPost(
                "Some sample post",
                "with this awesome content yo",
                ownerUser,
                false
        ));
    }
}
