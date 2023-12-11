/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author alkam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "user_id")
@Entity
@Table(name = "users")
public class User implements Serializable {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    private static final long serialVersionUID = 1L;

    public enum Role {
        ADMIN, CUSTOMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID user_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "username")
    private String username;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @ToString.Exclude
    @Column(name = "password")
    private String password;

    @NonNull
    @Email
    @Column(name = "email")
    private String email;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "salt")
    private String salt;

    @Null
    @Column(name = "profile_photo")
    private String profilePhoto;

    @NonNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Null
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Null
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Null
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}