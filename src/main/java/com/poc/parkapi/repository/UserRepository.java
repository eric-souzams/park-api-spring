package com.poc.parkapi.repository;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u.role from User u where u.username = :username")
    Optional<Role> findRoleByUsername(@Param("username") String username);
}