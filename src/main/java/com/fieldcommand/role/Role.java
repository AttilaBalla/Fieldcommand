package com.fieldcommand.role;


import com.fieldcommand.user.User;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "roles" )
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "power")
    private Integer power;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE)
    private Set<User> users = new HashSet<>();

    Role() {
    }

    public Role(RoleType roleType, int power){

        this.roleType = roleType;
        this.power = power;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return roleType;
    }

    private String getRoleString() {
        return roleType.toString();
    }

    public void setRole(RoleType role) {
        this.roleType = role;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return getRoleString();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}

