/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore;

/**
 *
 * @author alkam
 */
public class Constants {
    // Session attributes
    public static final String USER_SESSION_ATTRIBUTE = "user";

    // User attributes
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USERNAME_OR_EMAIL = "usernameOrEmail";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
    public static final String CREATED_AT = "createdAt";
    public static final String PHOTO = "photo";
    public static final String PROFILE_PHOTO = "profilePhoto";

    // User statistics
    public static final String USERS = "users";
    public static final String TOTAL_USERS = "totalUsers";
    public static final String NEW_USERS_THIS_MONTH = "newUsersThisMonth";

    // URLs
    public static final String LOGIN_URL = "/login";
    public static final String ADMIN_DASHBOARD = "/admin/dashboard";
    public static final String STORE = "/store";
    public static final String PROFILE = "/profile";
    public static final String LOGIN_VIEW = "/WEB-INF/views/login.jsp";
    public static final String DASHBOARD_VIEW = "/WEB-INF/views/dashboard.jsp";
    public static final String PROFILE_VIEW = "/WEB-INF/views/profile.jsp";
    public static final String REGISTER_VIEW = "/WEB-INF/views/register.jsp";
    public static final String USER_DIRECTORY = "/assets/img/users/";

    // Error messages
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String POST_REQUEST_ERROR = "Error processing POST request";
    public static final String DATABASE_ERROR = "Database error";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String USERNAME_TAKEN = "Username is already taken.";
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String EMAIL_REQUIRED = "Email is required.";
    public static final String EMAIL_IN_USE = "Email is already in use.";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";

    // Success messages
    public static final String REGISTRATION_SUCCESS = "You have been successfully registered.";
    public static final String SUCCESS_MESSAGE = "successMessage";

    // Date format
    public static final String DATE_FORMAT = "dd/MM/yyyy";
}