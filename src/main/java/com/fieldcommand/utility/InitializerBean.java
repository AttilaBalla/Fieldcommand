package com.fieldcommand.utility;

import com.fieldcommand.intrequest.InternalRequest;
import com.fieldcommand.intrequest.InternalRequestRepository;
import com.fieldcommand.intrequest.InternalRequestStatus;
import com.fieldcommand.newsfeed.NewsPost;
import com.fieldcommand.newsfeed.NewsPostRepository;
import com.fieldcommand.project.Project;
import com.fieldcommand.project.ProjectRepository;
import com.fieldcommand.role.Role;
import com.fieldcommand.role.RoleType;
import com.fieldcommand.user.User;
import com.fieldcommand.role.RoleRepository;
import com.fieldcommand.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InitializerBean {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private NewsPostRepository newsPostRepository;
    private ProjectRepository projectRepository;
    private InternalRequestRepository internalRequestRepository;

    private static User ownerUser;

    @Autowired
    public InitializerBean(RoleRepository roleRepository,
                           UserRepository userRepository,
                           NewsPostRepository newsPostRepository,
                           ProjectRepository projectRepository,
                           InternalRequestRepository internalRequestRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.newsPostRepository = newsPostRepository;
        this.projectRepository = projectRepository;
        this.internalRequestRepository = internalRequestRepository;

        initializeProjects();
        initializeRoles();
        initializeUsers();
        //initializeNews(); // keeping this if testing is required
        //initializeIntRequests();
    }

    private void initializeProjects() {

        if(projectRepository.count() == 0) {

            List<Project> projects = new ArrayList<>();

            projects.add(new Project("Rise of the Reds", "ROTR"));
            projects.add(new Project("SWR.net", "SWRNET"));
            projects.add(new Project("FieldCommand", "FC"));

            projectRepository.save(projects);
        }

    }

    private void initializeRoles() {

        if(roleRepository.count() == 0) {

            List<Role> roles = new ArrayList<>();

            roles.add(new Role(RoleType.ROLE_DISABLED, 0));
            roles.add(new Role(RoleType.ROLE_NEW, 1));
            roles.add(new Role(RoleType.ROLE_USER, 10));
            roles.add(new Role(RoleType.ROLE_DEVELOPER, 20));
            roles.add(new Role(RoleType.ROLE_ADMIN, 30));
            roles.add(new Role(RoleType.ROLE_OWNER, 40));

            roleRepository.save(roles);
        }

    }

    private void initializeUsers() {

        if(userRepository.count() == 0) {

            Set<Project> Projects = new HashSet<>();

            Role owner = roleRepository.findByRoleType(RoleType.ROLE_OWNER);
            Projects.add(projectRepository.findByShortName("FC"));
            Projects.add(projectRepository.findByShortName("ROTR"));
            Projects.add(projectRepository.findByShortName("SWRNET"));

            ownerUser = new User("xattus@gmail.com", "XAttus", owner);
            ownerUser.setPassword("$2a$10$9fQS0odOowHrEnZcpO93s.00RPWfdVrpoVpaSl3LpDE.z7RuxjVF6");
            ownerUser.setProjects(Projects);

            userRepository.save(ownerUser);
        }
    }

    private void initializeNews() {

        newsPostRepository.save(new NewsPost(
                "And this cool one comes from the DB",
                "with this awesome content that also comes from the DB :)",
                ownerUser,
                true
        ));
    }

    private void initializeIntRequests() {

        InternalRequest internalRequest = new InternalRequest(
                "Some sample request title",
                "Content here yo, we need to do dis!",
                ownerUser,
                InternalRequestStatus.WAITING,
                "ROTR");

        Set<User> supportingUsers = new HashSet<>();

        supportingUsers.add(ownerUser);

        internalRequest.setSupportingUsers(supportingUsers);
        internalRequest.setProject(projectRepository.findByShortName("ROTR"));

        internalRequestRepository.save(internalRequest);

    }
}
