package com.tutorial.spring.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles;
}
