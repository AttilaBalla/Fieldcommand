package com.fieldcommand.payload.user;

import java.util.ArrayList;
import java.util.List;

public class UpdateJson {

    private String username;
    private String email;
    private Long id;
    private String role;
    private List<String> projects = new ArrayList<>();

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

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        StringBuilder updateJson = new StringBuilder("username: " + this.username + "\n"
                + "email: " + this.email + "\n"
                + "id: " + this.id + "\n"
                + "role: " + this.role + "\n");

        for (String project: this.projects
             ) {
            updateJson.append(project).append(", ");
        }
        return updateJson.toString();
    }
}
