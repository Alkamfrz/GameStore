/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

/**
 *
 * @author alkam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Role {
        ADMIN, CUSTOMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

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

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isCustomer() {
        return role == Role.CUSTOMER;
    }
}