package com.fieldcommand.json.user;

import java.util.ArrayList;
import java.util.List;

public class UpdateJson {

    private String username;
    private String email;
    private Long id;
    private String role;
    private List<String> versions = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        StringBuilder updateJson = new StringBuilder("username: " + this.username + "\n"
                + "email: " + this.email + "\n"
                + "id: " + this.id + "\n"
                + "role: " + this.role + "\n");

        for (String version: this.versions
             ) {
            updateJson.append(version).append(", ");
        }
        return updateJson.toString();
    }
}
