package com.fieldcommand.repository;

import com.fieldcommand.model.Role;
import com.fieldcommand.model.RoleType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRole(RoleType role);
    List<Role> findAll();
}
