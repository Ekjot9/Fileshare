package com.fileshare.model;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    
    private String password;

    @Column(nullable = false)
    private String role = "USER";  // default role

    @ManyToOne
@JoinColumn(name = "team_id")
private Team team;
public void setTeam(Team team) {
    this.team = team;
}

public Team getTeam() {
    return this.team;
}

    // ✅ Default constructor
   public User() {
     }

// ✅ Parameterized constructor
public User(String username, String password) {
    this.username = username;
    this.password = password;
}


    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
}