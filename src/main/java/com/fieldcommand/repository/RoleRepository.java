package com.fieldcommand.repository;

import com.fieldcommand.model.Role;
import com.fieldcommand.model.RoleType;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRole(RoleType role);

    //@Query("select Role from User where user_id = 1")
    //List<Role> findRolesByUserId(Long id);
}
