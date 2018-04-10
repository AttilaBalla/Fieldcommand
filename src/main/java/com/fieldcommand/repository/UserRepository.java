package com.fieldcommand.repository;

import com.fieldcommand.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmail(String email);

    @Query("select u from User u")
    List<User> findAll();
}
