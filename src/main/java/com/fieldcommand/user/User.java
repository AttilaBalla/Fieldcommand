package com.fieldcommand.user;

import com.fieldcommand.intrequest.InternalRequest;
import com.fieldcommand.newsfeed.NewsPost;
import com.fieldcommand.project.Project;
import com.fieldcommand.role.Role;
import org.hibernate.validator.constraints.NotBlank;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique=true)
    @NotBlank
    private String username;

    @Column
    private String password;

    @Column(unique=true, nullable=false)
    @NotBlank
    private String email;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE)
    private Set<NewsPost> newsPosts = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE)
    private Set<InternalRequest> internalRequests = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "user_project",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private Set<Project> projects = new HashSet<>();

    private String activationKey;

    public User()
    {}

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(String email, String username, Role role) {
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public User(String email, String username, Role role, String activationKey) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.activationKey = activationKey;
    }

    HashMap<String, Object> getSimpleUserDetails() {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("id", id.toString());
        userData.put("username", username);
        userData.put("email", email);
        userData.put("role", role.toString());
        userData.put("rolePower", role.getPower().toString());
        userData.put("projects", getProjectStringList());

        return userData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getRoleString() {
        return role.toString();
    }

    public Role getRole() {
        return role;
    }

    void setRole(Role role) {
        this.role = role;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    void setActivationKey(String key) {
        this.activationKey = key;
    }

    public Set<NewsPost> getNewsPosts() {
        return newsPosts;
    }

    public void setNewsPosts(Set<NewsPost> newsPosts) {
        this.newsPosts = newsPosts;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    List<String> getProjectStringList() {

        List<String> projectList = new ArrayList<>();

        for (Project project: projects
             ) {
            projectList.add(project.getShortName());
        }

        return projectList;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }
}
