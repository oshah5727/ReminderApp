package com.studious.studious.model;

import jakarta.persistence.*;

// @Entity tells Spring/Hibernate that this class represents a database table.
// Every time the app starts, Hibernate checks if the "users" table exists.
// If it doesn't, it creates it automatically based on the fields below.
@Entity
@Table(name = "users")  // The actual table name in the H2 database will be "users"
public class User {

    // @Id marks this field as the primary key (unique identifier for each row).
    // @GeneratedValue means the database auto-increments this number (1, 2, 3...)
    // so we never have to set it manually.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = false means this column CANNOT be empty in the database.
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // unique = true means no two users can share the same email address.
    @Column(nullable = false, unique = true)
    private String email;

    // unique = true means no two users can share the same username.
    @Column(nullable = false, unique = true)
    private String username;

    // This stores the HASHED password, never the plain-text one.
    @Column(nullable = false)
    private String password;

    // Getters and setters — these are how other classes read and write
    // the values of each field. Spring/Hibernate also uses them internally.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
