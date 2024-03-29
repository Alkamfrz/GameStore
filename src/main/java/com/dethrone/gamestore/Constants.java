package com.dethrone.gamestore;

public class Constants {
    // Session attributes
    public static final String USER_SESSION_ATTRIBUTE = "user";

    // User attributes
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CURRENT_PASSWORD = "currentPassword";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String CONFIRM_PASSWORD = "confirmPassword";
    public static final String USERNAME_OR_EMAIL = "usernameOrEmail";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String DELETED_AT = "deletedAt";
    public static final String LAST_LOGIN = "lastLogin";
    public static final String PHOTO = "photo";
    public static final String PROFILE_PHOTO = "profilePhoto";

    // Transaction attributes
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String TRANSACTION_STATUS = "transaction_status";

    // Game attributes
    public static final String GAMES = "games";
    public static final String GAME_ID = "game_id";
    public static final String GAME_NAME = "game_name";
    public static final String GAME_PRICE = "game_price";
    public static final String GAME_DESCRIPTION = "game_description";
    public static final String GAME_IMAGE = "game_image";
    public static final String GAME_PUBLISHER = "game_publisher";
    public static final String GAME_GENRE = "game_genre";
    public static final String GAME_RELEASE_DATE = "game_release_date";
    public static final String GAME_RATING = "game_rating";
    
    // Genre attributes
    public static final String GENRES = "genres";
    public static final String GENRE_ID = "genre_id";
    public static final String GENRE_NAME = "genre_name";
    public static final String GENRE_DESCRIPTION = "genre_description";

    // Publisher attributes
    public static final String PUBLISHERS = "publishers";
    public static final String PUBLISHER_ID = "publisher_id";
    public static final String PUBLISHER_NAME = "publisher_name";
    public static final String PUBLISHER_COUNTRY = "publisher_country";
    public static final String PUBLISHER_CITY = "publisher_city";
    public static final String PUBLISHER_STREET = "publisher_street";
    public static final String PUBLISHER_PHONE = "publisher_phone";
    public static final String PUBLISHER_EMAIL = "publisher_email";
    public static final String PUBLISHER_WEBSITE = "publisher_website";

    // User statistics
    public static final String USERS = "users";
    public static final String TOTAL_USERS = "totalUsers";
    public static final String NEW_USERS_THIS_MONTH = "newUsersThisMonth";

    // Transaction statistics
    public static final String TRANSACTIONS = "transactions";
    public static final String TOTAL_TRANSACTIONS = "totalTransactions";
    public static final String TOTAL_SALES = "totalSales";
    public static final String SALES_THIS_MONTH = "salesThisMonth";

    // URLs
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String ADMIN_DASHBOARD = "/admin/dashboard";
    public static final String STORE = "/store";
    public static final String PROFILE = "/profile";
    public static final String LOGIN_VIEW = "/WEB-INF/views/login.jsp";
    public static final String DASHBOARD_VIEW = "/WEB-INF/views/dashboard.jsp";
    public static final String PROFILE_VIEW = "/WEB-INF/views/profile/profile.jsp";
    public static final String REGISTER_VIEW = "/WEB-INF/views/register.jsp";
    public static final String USERS_VIEW = "/WEB-INF/views/users.jsp";
    public static final String STORE_VIEW = "/WEB-INF/views/store.jsp";
    public static final String GAME_VIEW = "/WEB-INF/views/game.jsp";
    public static final String GENRE_VIEW = "/WEB-INF/views/genre.jsp";
    public static final String PUBLISHER_VIEW = "/WEB-INF/views/publisher.jsp";
    public static final String USER_DIRECTORY = "/assets/user_profile/";
    public static final String USER_IMAGE_DIRECTORY = "/images/";

    // Error messages
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String DATE_PARSE_ERROR = "Error parsing date";
    public static final String GET_REQUEST_ERROR = "Error processing GET request";
    public static final String POST_REQUEST_ERROR = "Error processing POST request";
    public static final String DATABASE_ERROR = "Database error";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String USERNAME_TAKEN = "Username is already taken.";
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String PASSWORD_MISMATCH = "Passwords do not match.";
    public static final String CURRENT_PASSWORD_MISMATCH = "Current password is incorrect.";
    public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required.";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required.";
    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required.";
    public static final String EMAIL_REQUIRED = "Email is required.";
    public static final String EMAIL_IN_USE = "Email is already in use.";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";

    // Success messages
    public static final String REGISTRATION_SUCCESS = "You have been successfully registered.";
    public static final String SUCCESS_MESSAGE = "successMessage";
    public static final String PASSWORD_CHANGED = "Password has been successfully changed.";

    // Date format
    public static final String DATE_FORMAT = "dd/MM/yyyy";
}