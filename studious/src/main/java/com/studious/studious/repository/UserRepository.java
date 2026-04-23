package com.studious.studious.repository;

import com.studious.studious.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// UserRepository is our "bridge" between the app and the database.
// By extending JpaRepository, Spring automatically gives us built-in methods
// like save(), findById(), findAll(), delete() — no SQL needed.
// The two type parameters mean: the entity is User, and its primary key type is Long.
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring reads the method name and automatically generates the SQL:
    // SELECT * FROM users WHERE username = ?
    // Returns Optional<User> so we can safely handle the case where no user is found.
    Optional<User> findByUsername(String username);

    // SELECT COUNT(*) FROM users WHERE username = ? — returns true if any row exists.
    // Used during sign-up to block duplicate usernames.
    boolean existsByUsername(String username);

    // Same pattern — checks whether an email is already registered.
    boolean existsByEmail(String email);
}
