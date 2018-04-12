package com.fieldcommand.service;

import com.fieldcommand.model.Role;
import com.fieldcommand.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;

    }

    public List<HashMap<String, String>> findAll() {
        List<Role> roles = roleRepository.findAll();
        List<HashMap<String, String>> roleData = new ArrayList<>();

        for (Role role: roles) {
            roleData.add(role.convertToMap());
        }

        return roleData;
    }

}
