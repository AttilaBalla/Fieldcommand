package com.fieldcommand.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    User findUserByActivationKey(String key);

    User findUserById(Long id);

    @Query( nativeQuery = true, value = "SELECT COUNT(*) FROM users WHERE role_id > 2")
    int countActiveUsers();

}
