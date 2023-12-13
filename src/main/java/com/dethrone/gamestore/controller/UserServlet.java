/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;

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
                        response.getWriter().write("{\"firstName\": \"" + user.getFirstName() + "\", \"lastName\": \""
                                + user.getLastName() + "\", \"username\": \"" + user.getUsername() + "\", \"email\": \""
                                + user.getEmail() + "\", \"role\": \"" + user.getRole() + "\"}");
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"status\": \"failure\"}");
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
                if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    String userIdStr = request.getParameter("user_id");
                    if (userIdStr == null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"status\": \"failure\", \"message\": \"User ID is missing\"}");
                        return;
                    }
                    UUID userId = UUID.fromString(userIdStr);
                    try {
                        if (pathInfo.equals("/edit")) {
                            String newFirstName = request.getParameter("firstName");
                            String newLastName = request.getParameter("lastName");
                            String newUsername = request.getParameter("username");
                            String newEmail = request.getParameter("email");
                            String newRole = request.getParameter("role");

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
                                response.getWriter().write("{\"status\": \"success\"}");
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                                response.getWriter()
                                        .write("{\"status\": \"failure\", \"message\": \"User not found\"}");
                            }
                        } else if (pathInfo.equals("/delete")) {
                            Optional<User> userToDeleteOpt = userService.getUserById(userId);
                            if (userToDeleteOpt.isPresent()) {
                                User userToDelete = userToDeleteOpt.get();

                                userService.deleteUser(userToDelete);
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                                response.getWriter().write("{\"status\": \"success\"}");
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");
                                response.getWriter()
                                        .write("{\"status\": \"failure\", \"message\": \"User not found\"}");
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(Constants.DATABASE_ERROR, e);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                                "{\"status\": \"failure\", \"message\": \"Database error: " + e.getMessage() + "\"}");
                    }
                }
            } else {
                response.sendRedirect(request.getContextPath() + Constants.STORE_VIEW);
            }
        } else {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        }
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
