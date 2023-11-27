/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author alkam
 */
public class UserService {

    private void executeInTransaction(Consumer<Session> action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            action.accept(session);
            session.getTransaction().commit();
        }
    }

    public User registerUser(User user) {
        validatePassword(user.getPassword());
        user.hashPassword(user.getPassword());
        executeInTransaction(session -> session.persist(user));
        return user;
    }

    public Optional<User> loginUser(String usernameOrEmail, String password) {
        return getUser(usernameOrEmail)
                .filter(user -> user.checkPassword(password));
    }

    private Optional<User> getUser(String usernameOrEmail) {
        return Optional.ofNullable(getUserByUsername(usernameOrEmail)
                .orElseGet(() -> getUserByEmail(usernameOrEmail).orElse(null)));
    }

    public Optional<User> getUserByQuery(String query, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery(query, User.class).setParameter("value", value)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return getUserByQuery("from User where username = :value", username);
    }

    public Optional<User> getUserByEmail(String email) {
        return getUserByQuery("from User where email = :value", email);
    }

    public Optional<User> getUserById(int id) {
        return getUserByQuery("from User where id = :value", id);
    }

    public List<User> getAllUsers() {
        List<User> users;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            users = session.createQuery("from User", User.class).list();
        }
        return users;
    }

    public void updateUser(User user) {
        executeInTransaction(session -> session.merge(user));
    }

    public void deleteUser(User user) {
        executeInTransaction(session -> session.remove(user));
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user.checkPassword(oldPassword)) {
            user.changePassword(newPassword);
            updateUser(user);
        } else {
            throw new IllegalArgumentException("Old password does not match");
        }
    }

    public void changeEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        updateUser(user);
    }

    public void changeRole(User user, User.Role newRole) {
        user.setRole(newRole);
        updateUser(user);
    }

    public void changeUsername(User user, String newUsername) {
        user.setUsername(newUsername);
        updateUser(user);
    }

    public void changeFirstName(User user, String newFirstName) {
        user.setFirstName(newFirstName);
        updateUser(user);
    }

    public void changeLastName(User user, String newLastName) {
        user.setLastName(newLastName);
        updateUser(user);
    }

    public void changeUser(User user, String newUsername, String newFirstName, String newLastName, String newEmail,
            User.Role newRole) {
        user.setUsername(newUsername);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setEmail(newEmail);
        user.setRole(newRole);
        updateUser(user);
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}