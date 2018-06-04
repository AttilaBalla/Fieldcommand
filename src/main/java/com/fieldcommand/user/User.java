package com.fieldcommand.user;

import com.fieldcommand.newsfeed.Newspost;
import com.fieldcommand.role.Role;
import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    @Column
    private String password;

    @Column(unique=true, nullable=false)
    private String email;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE)
    private Set<Newspost> newsPosts = new HashSet<>();

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

    public HashMap<String, String> getSimpleUserDetails() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("id", id.toString());
        userData.put("username", username);
        userData.put("email", email);
        userData.put("role", role.toString());

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

    public String getRoleString() {
        return role.toString();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;

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
