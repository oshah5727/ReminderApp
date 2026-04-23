package com.studious.studious.service;

import com.studious.studious.model.User;
import com.studious.studious.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// @Service marks this class as a "business logic" layer.
// It sits between the controller (handles web requests) and the repository (talks to the DB).
// The controller calls methods here; this class does the actual work.
@Service
public class UserService {

    // Spring automatically injects the UserRepository so we can query the database.
    @Autowired
    private UserRepository userRepository;

    // BCryptPasswordEncoder is a one-way hashing algorithm.
    // It scrambles a plain-text password into a long random-looking string.
    // You can NEVER reverse it back to the original — that's the point.
    // Example: "mypassword" → "$2a$10$XurXv4qY3k...(long hash)"
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * register() — called when a user submits the Sign Up form.
     *
     * How it works:
     * 1. Check if the username is already in the database. If yes, stop and throw an error.
     * 2. Check if the email is already in the database. If yes, stop and throw an error.
     * 3. Create a new User object and fill in all the fields.
     * 4. Hash the password before storing it (we NEVER save plain-text passwords).
     * 5. Save the user to the database via the repository.
     */
    public void register(String firstName, String lastName, String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        // passwordEncoder.encode() hashes the password before it goes into the database
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);  // Writes the new row to the "users" table
    }

    /**
     * authenticate() — called when a user submits the Login form.
     *
     * How it works:
     * 1. Look up the user by username in the database.
     *    If no user is found, throw an error (same generic message to avoid hinting which field is wrong).
     * 2. Use passwordEncoder.matches() to compare the plain-text password the user typed
     *    against the stored hash. BCrypt re-hashes the input and compares — if they match, it returns true.
     * 3. If the password is wrong, throw an error.
     * 4. If everything is correct, return the User object so the controller can start a session.
     */
    public User authenticate(String username, String password) {
        // orElseThrow means: if findByUsername returns empty (no user found), throw the error
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        // passwordEncoder.matches() compares raw input to the stored hash — they never have to be equal strings
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }
}
