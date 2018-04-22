package com.fieldcommand.user;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    User findUserByActivationKey(String key);

    User findUserById(Long id);
}
