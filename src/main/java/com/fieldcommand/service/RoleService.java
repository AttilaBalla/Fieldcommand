package com.fieldcommand.service;

import com.fieldcommand.model.Role;
import com.fieldcommand.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
        initializeRoles();

    }

    private void initializeRoles() {

        List<Role> roles = new ArrayList<>();

        roles.add(new Role("ROLE_DISABLED", 0));
        roles.add(new Role("ROLE_NEW", 1));
        roles.add(new Role("ROLE_USER", 10));
        roles.add(new Role("ROLE_DEVELOPER", 20));
        roles.add(new Role("ROLE_ADMIN", 30));
        roles.add(new Role("ROLE_OWNER", 40));

        roleRepository.save(roles);

    }

}
