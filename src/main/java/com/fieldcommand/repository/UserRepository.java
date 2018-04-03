package com.fieldcommand.repository;

import com.fieldcommand.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

    User findUserByEmail(String email);
}
