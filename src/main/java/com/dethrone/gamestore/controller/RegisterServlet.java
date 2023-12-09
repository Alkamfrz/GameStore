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

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alkam
 */
@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServlet.class);

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
        try {
            String username = Optional.ofNullable(request.getParameter(Constants.USERNAME)).orElse("");
            String firstName = Optional.ofNullable(request.getParameter(Constants.FIRST_NAME)).orElse("");
            String lastName = Optional.ofNullable(request.getParameter(Constants.LAST_NAME)).orElse("");
            String password = Optional.ofNullable(request.getParameter(Constants.PASSWORD)).orElse("");
            String email = Optional.ofNullable(request.getParameter(Constants.EMAIL)).orElse("");

            firstName = Arrays.stream(firstName.split(" "))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));
            lastName = Arrays.stream(lastName.split(" "))
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));

            List<String> errorMessage = validateFormData(username, firstName, lastName, password, email);

            if (errorMessage != null && !errorMessage.isEmpty()) {
                request.setAttribute(Constants.ERROR_MESSAGE, errorMessage);
                RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.REGISTER_VIEW);
                dispatcher.forward(request, response);
            } else {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);

                userService.registerUser(user);
                request.getSession().setAttribute(Constants.SUCCESS_MESSAGE, Constants.REGISTRATION_SUCCESS);
                response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
            }
        } catch (Exception e) {
            LOGGER.error(Constants.POST_REQUEST_ERROR, e);
        }
    }

    private List<String> validateFormData(String username, String firstName, String lastName, String password,
            String email) {
        List<String> errorMessages = new ArrayList<>();
        if (username.isEmpty() || userService.isUsernameExist(username) || firstName.isEmpty() || lastName.isEmpty()
                || password.isEmpty() || email.isEmpty() || userService.isEmailExist(email)) {
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "RegisterServlet";
    }

}
