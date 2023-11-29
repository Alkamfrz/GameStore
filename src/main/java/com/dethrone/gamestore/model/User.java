/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

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

    public void hashPassword(String password) {
        this.salt = generateSalt();
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(10)
                .withMemoryPowOfTwo(16)
                .withParallelism(1)
                .withSalt(salt.getBytes());

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());

        byte[] result = new byte[32];
        gen.generateBytes(password.toCharArray(), result);

        this.password = new String(result);
    }

    public void changePassword(String newPassword) {
        hashPassword(newPassword);
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public boolean checkPassword(String password) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(10)
                .withMemoryPowOfTwo(16)
                .withParallelism(1)
                .withSalt(salt.getBytes());

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());

        byte[] result = new byte[32];
        gen.generateBytes(password.toCharArray(), result);

        return this.password.equals(new String(result));
    }
}