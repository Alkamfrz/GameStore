/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alkam
 */
@WebServlet(name = "UserServlet", urlPatterns = { "/admin/users/*" })
public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        userService = (UserService) context.getAttribute("userService");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (isUserLoggedIn(session)) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.getRole().equals(User.Role.ADMIN)) {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    UUID userId = UUID.fromString(pathInfo.substring(1));
                    Optional<User> userOpt = userService.getUserById(userId);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject userJson = new JsonObject();
                        userJson.addProperty("firstName", user.getFirstName());
                        userJson.addProperty("lastName", user.getLastName());
                        userJson.addProperty("username", user.getUsername());
                        userJson.addProperty("email", user.getEmail());
                        userJson.addProperty("role", user.getRole().toString());
                        userJson.addProperty("currentUserId", currentUser.getUser_id().toString());

                        response.getWriter().write(userJson.toString());
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");

                        response.getWriter().write(errorJson.toString());
                    }
                } else {
                    setUserAttributes(request, currentUser);
                    setUserData(request);
                    request.getRequestDispatcher(Constants.USERS_VIEW).forward(request, response);
                }
            } else {
                response.sendRedirect(request.getContextPath() + Constants.STORE_VIEW);
            }
        } else {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (isUserLoggedIn(session)) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.getRole().equals(User.Role.ADMIN)) {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null && pathInfo.equals("/add")) {
                    BufferedReader reader = request.getReader();
                    Gson gson = new Gson();
                    Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    String username = ((String) data.get(Constants.USERNAME)).toLowerCase();
                    String firstName = (String) data.get(Constants.FIRST_NAME);
                    String lastName = (String) data.get(Constants.LAST_NAME);
                    String password = (String) data.get(Constants.PASSWORD);
                    String email = ((String) data.get(Constants.EMAIL)).toLowerCase();
                    String role = (String) data.get(Constants.ROLE);

                    firstName = Arrays.stream(firstName.split(" "))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                            .collect(Collectors.joining(" "));
                    lastName = Arrays.stream(lastName.split(" "))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                            .collect(Collectors.joining(" "));

                    List<String> errorMessage = validateFormData(username, firstName, lastName, password, email);

                    if (errorMessage != null && !errorMessage.isEmpty()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", String.join(", ", errorMessage));

                        response.getWriter().write(errorJson.toString());
                    } else {
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setRole(User.Role.valueOf(role.toUpperCase()));

                        userService.registerUser(user);

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject successJson = new JsonObject();
                        successJson.addProperty("status", "success");
                        successJson.addProperty("message", Constants.REGISTRATION_SUCCESS);

                        response.getWriter().write(successJson.toString());
                    }
                }
                if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    BufferedReader reader = request.getReader();
                    Gson gson = new Gson();
                    Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    String userIdStr = (String) data.get("user_id");
                    if (userIdStr == null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "User ID is missing");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }
                    UUID userId = UUID.fromString(userIdStr);
                    if (pathInfo.equals("/delete") && currentUser.getUser_id().equals(userId)) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "You cannot delete yourself");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }
                    try {
                        if (pathInfo.equals("/edit")) {
                            String newFirstName = (String) data.get("firstName");
                            String newLastName = (String) data.get("lastName");
                            String newUsername = ((String) data.get("username")).toLowerCase();
                            String newEmail = ((String) data.get("email")).toLowerCase();
                            String newRole = (String) data.get("role");

                            Optional<User> userToEditOpt = userService.getUserById(userId);
                            if (userToEditOpt.isPresent()) {
                                User userToEdit = userToEditOpt.get();

                                userToEdit.setFirstName(newFirstName);
                                userToEdit.setLastName(newLastName);
                                userToEdit.setUsername(newUsername);
                                userToEdit.setEmail(newEmail);
                                userToEdit.setRole(User.Role.valueOf(newRole.toUpperCase()));

                                userService.updateUser(userToEdit);

                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject successJson = new JsonObject();
                                successJson.addProperty("status", "success");

                                response.getWriter().write(successJson.toString());
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject errorJson = new JsonObject();
                                errorJson.addProperty("status", "failure");
                                errorJson.addProperty("message", "User not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        } else if (pathInfo.equals("/delete")) {
                            Optional<User> userToDeleteOpt = userService.getUserById(userId);
                            if (userToDeleteOpt.isPresent()) {
                                User userToDelete = userToDeleteOpt.get();

                                userService.deleteUser(userToDelete);

                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject successJson = new JsonObject();
                                successJson.addProperty("status", "success");

                                response.getWriter().write(successJson.toString());
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject errorJson = new JsonObject();
                                errorJson.addProperty("status", "failure");
                                errorJson.addProperty("message", "User not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(Constants.DATABASE_ERROR, e);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Database error: " + e.getMessage());

                        response.getWriter().write(errorJson.toString());
                    }
                }
            } else {
                response.sendRedirect(request.getContextPath() + Constants.STORE_VIEW);
            }
        } else {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        }
    }

    private List<String> validateFormData(String username, String firstName, String lastName, String password,
            String email) {
        List<String> errorMessages = new ArrayList<>();
        if (username.isEmpty() || userService.isUsernameExist(username) || firstName.isEmpty() || lastName.isEmpty()
                || password.isEmpty() || email.isEmpty()
                || userService.isEmailExist(email)) {
            if (username.isEmpty()) {
                errorMessages.add(Constants.USERNAME_REQUIRED);
            } else if (userService.isUsernameExist(username)) {
                errorMessages.add(Constants.USERNAME_TAKEN);
            }
            if (firstName.isEmpty()) {
                errorMessages.add(Constants.FIRST_NAME_REQUIRED);
            }
            if (lastName.isEmpty()) {
                errorMessages.add(Constants.LAST_NAME_REQUIRED);
            }
            if (password.isEmpty()) {
                errorMessages.add(Constants.PASSWORD_REQUIRED);
            }
            if (email.isEmpty()) {
                errorMessages.add(Constants.EMAIL_REQUIRED);
            } else if (userService.isEmailExist(email)) {
                errorMessages.add(Constants.EMAIL_IN_USE);
            }
        }
        return errorMessages;
    }

    private boolean isUserLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    private void setUserAttributes(HttpServletRequest request, User currentUser) {
        String username = currentUser.getUsername();
        UUID user_id = currentUser.getUser_id();
        String role = currentUser.getRole().toString().toLowerCase();

        request.setAttribute(Constants.USERNAME, username);
        request.setAttribute(Constants.USER_ID, user_id);
        request.setAttribute(Constants.ROLE, role);
    }

    private void setUserData(HttpServletRequest request) throws ServletException {
        List<User> users = userService.getAllUsers();
        users.sort((User u1, User u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()));
        request.setAttribute(Constants.USERS, users);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "UserServlet";
    }
}
