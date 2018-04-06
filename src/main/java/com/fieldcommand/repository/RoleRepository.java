package com.fieldcommand.repository;

import com.fieldcommand.model.Role;
import org.springframework.data.repository.CrudRepository;


public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRole(String role);
}
