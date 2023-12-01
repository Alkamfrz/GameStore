/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author alkam
 */
public class UserService {

    private final SecurityService securityService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String QUERY_BY_USERNAME = "from User where username = :value";
    private static final String QUERY_BY_EMAIL = "from User where email = :value";
    private static final String QUERY_BY_ID = "from User where id = :value";

    public UserService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public class CustomNonUniqueResultException extends RuntimeException {
        public CustomNonUniqueResultException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private void executeInTransaction(Consumer<Session> action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                action.accept(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    public User registerUser(User user) {
        if (!securityService.validatePassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (user.getSalt() == null) {
            String salt = securityService.generateSalt();
            String hashedPassword = securityService.hashPassword(user.getPassword(), salt);
            user.setPassword(hashedPassword);
            user.setSalt(salt);
        }
        executeInTransaction(session -> session.persist(user));
        return user;
    }

    public Optional<User> loginUser(String usernameOrEmail, String password) {
        Optional<User> user = getUser(usernameOrEmail);
        if (!user.isPresent()) {
            LOGGER.error("User not found");
            return Optional.empty();
        }
        return user.filter(u -> securityService.checkPassword(password, u.getPassword(), u.getSalt()));
    }

    private Optional<User> getUser(String usernameOrEmail) {
        return getUserByQuery(QUERY_BY_USERNAME, usernameOrEmail)
                .or(() -> getUserByQuery(QUERY_BY_EMAIL, usernameOrEmail));
    }

    private <T> T executeQuery(Function<Session, T> query) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return query.apply(session);
        }
    }

    public Optional<User> getUserByQuery(String query, Object value) {
        return executeQuery(session -> {
            List<User> users = session.createQuery(query, User.class).setParameter("value", value).getResultList();
            if (users.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(users.get(0));
            }
        });
    }

    public Optional<User> getUserByUsername(String username) {
        return getUserByQuery(QUERY_BY_USERNAME, username);
    }

    public Optional<User> getUserByEmail(String email) {
        return getUserByQuery(QUERY_BY_EMAIL, email);
    }

    public Optional<User> getUserById(int id) {
        return getUserByQuery(QUERY_BY_ID, id);
    }

    public List<User> getAllUsers() {
        return executeQuery(session -> session.createQuery("from User", User.class).list());
    }

    public void updateUser(User user) {
        executeInTransaction(session -> session.merge(user));
    }

    public void deleteUser(User user) {
        executeInTransaction(session -> session.remove(user));
    }

    public void changeUserProperty(User user, Consumer<User> propertyChanger) {
        propertyChanger.accept(user);
        updateUser(user);
        LOGGER.info("User property changed successfully");
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (securityService.checkPassword(oldPassword, user.getPassword(), user.getSalt())) {
            securityService.changePassword(user, newPassword);
            updateUser(user);
            LOGGER.info("Password changed successfully");
        } else {
            LOGGER.error("Old password does not match");
            throw new IllegalArgumentException("Old password does not match");
        }
    }

    public void changeEmail(User user, String newEmail) {
        changeUserProperty(user, u -> u.setEmail(newEmail));
    }

    public void changeRole(User user, User.Role newRole) {
        changeUserProperty(user, u -> u.setRole(newRole));
    }

    public void changeUsername(User user, String newUsername) {
        changeUserProperty(user, u -> u.setUsername(newUsername));
    }

    public void changeFirstName(User user, String newFirstName) {
        changeUserProperty(user, u -> u.setFirstName(newFirstName));
    }

    public void changeLastName(User user, String newLastName) {
        changeUserProperty(user, u -> u.setLastName(newLastName));
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

    public boolean isUsernameExist(String username) {
        return getUserByUsername(username).isPresent();
    }

    public boolean isEmailExist(String email) {
        return getUserByEmail(email).isPresent();
    }
}