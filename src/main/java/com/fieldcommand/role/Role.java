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

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "power")
    private Integer power;

    @ManyToMany( mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    Role() {
    }

    public Role( RoleType role, int power){

        this.role = role;
        this.power = power;
    }

    public Long getId() {
        return id;
    }

    public HashMap<String, String> convertToMap() {
        HashMap<String, String> roleData = new HashMap<>();
        roleData.put("roleType", role.toString());
        roleData.put("power", power.toString());

        return roleData;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRole() {
        return role;
    }

    public String getRoleString() {
        return role.toString();
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "Role id: " + id + ", Role: " + role + " Role power: " + power;
    }
}

