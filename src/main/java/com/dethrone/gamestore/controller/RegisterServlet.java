/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.SecurityService;
import com.dethrone.gamestore.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author alkam
 */
@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {

    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String REGISTER_VIEW = "/WEB-INF/views/register.jsp";
    private static final String LOGIN_REDIRECT = "/login";

    SecurityService securityService = new SecurityService();
    UserService userService = new UserService(securityService);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

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
        processRequest(request, response);
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
        String username = Optional.ofNullable(request.getParameter(USERNAME)).orElse("");
        String firstName = Optional.ofNullable(request.getParameter(FIRST_NAME)).orElse("");
        String lastName = Optional.ofNullable(request.getParameter(LAST_NAME)).orElse("");
        String password = Optional.ofNullable(request.getParameter(PASSWORD)).orElse("");
        String email = Optional.ofNullable(request.getParameter(EMAIL)).orElse("");

        firstName = Arrays.stream(firstName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        lastName = Arrays.stream(lastName.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        List<String> errorMessage = validateFormData(username, firstName, lastName, password, email);

        if (errorMessage != null && !errorMessage.isEmpty()) {
            request.setAttribute(ERROR_MESSAGE, errorMessage);
            RequestDispatcher dispatcher = request.getRequestDispatcher(REGISTER_VIEW);
            dispatcher.forward(request, response);
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            userService.registerUser(user);
            request.getSession().setAttribute(SUCCESS_MESSAGE, "You have been successfully registered.");
            response.sendRedirect(request.getContextPath() + LOGIN_REDIRECT);
        }
    }

    private List<String> validateFormData(String username, String firstName, String lastName, String password,
            String email) {
        List<String> errorMessages = new ArrayList<>();
        if (username.isEmpty() || userService.isUsernameExist(username) || firstName.isEmpty() || lastName.isEmpty()
                || password.isEmpty() || email.isEmpty() || userService.isEmailExist(email)) {
            if (username.isEmpty()) {
                errorMessages.add("Username is required.");
            } else if (userService.isUsernameExist(username)) {
                errorMessages.add("Username is already taken.");
            }
            if (firstName.isEmpty()) {
                errorMessages.add("First name is required.");
            }
            if (lastName.isEmpty()) {
                errorMessages.add("Last name is required.");
            }
            if (password.isEmpty()) {
                errorMessages.add("Password is required.");
            }
            if (email.isEmpty()) {
                errorMessages.add("Email is required.");
            } else if (userService.isEmailExist(email)) {
                errorMessages.add("Email is already in use.");
            }
        }
        return errorMessages;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
