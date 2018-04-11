package com.fieldcommand.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<Role>();

    @Column
    private String password;

    @Column(unique=true, nullable=false)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType displayedRole;

    private String activationKey;

    public User()
    {}

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(String email, String username, Role role, String activationKey) {
        this.email = email;
        this.username = username;
        this.addRole(role);
        this.activationKey = activationKey;
    }

    private void findDisplayedRole() {
        int highest = 0;
        for (Role role: roles
             ) {
                int power = role.getPower();
            if (power > highest) {
                highest = power;
                this.displayedRole = role.getRole();
            }
        }
    }

    public HashMap<String, String> convertToMap() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("id", id.toString());
        userData.put("username", username);
        userData.put("email", email);
        userData.put("role", displayedRole.toString());

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
        this.findDisplayedRole();
    }

    public void addRole(Role role) {
        if (this.roles == null || this.roles.isEmpty()) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
        this.findDisplayedRole();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    public void setActivationKey(String key) {
        this.activationKey = key;
    }

    public RoleType getDisplayedRole() {
        return displayedRole;
    }

    public void setDisplayedRole(RoleType displayedRole) {
        this.displayedRole = displayedRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", email='" + email + '\'' +
                '}';
    }
}
