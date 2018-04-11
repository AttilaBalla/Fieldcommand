package com.fieldcommand.repository;

import com.fieldcommand.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmail(String email);

    List<User> findAll();
}
