package com.fieldcommand.repository;

import com.fieldcommand.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserWriteRepository extends CrudRepository<User, Long> {

    User findUserByEmail(String email);
}
