/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.util.Optional;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;

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
@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);

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
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
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
            String usernameOrEmail = request.getParameter(Constants.USERNAME_OR_EMAIL);
            String password = request.getParameter(Constants.PASSWORD);

            Optional<User> user = userService.loginUser(usernameOrEmail, password);

            if (user.isPresent()) {
                request.getSession().setAttribute("user", user.get());
                redirectToPageBasedOnRole(user.get(), request, response);
            } else {
                handleInvalidLogin(request, response);
            }
        } catch (Exception e) {
            LOGGER.error(Constants.POST_REQUEST_ERROR, e);
        }
    }

    private void redirectToPageBasedOnRole(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String role = user.getRole().name().toLowerCase();

        if ("admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + Constants.ADMIN_DASHBOARD);
        } else if ("customer".equals(role)) {
            response.sendRedirect(request.getContextPath() + Constants.STORE);
        } else {
            handleInvalidLogin(request, response);
        }
    }

    private void handleInvalidLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(Constants.ERROR_MESSAGE, Constants.INVALID_CREDENTIALS);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "LoginServlet";
    }

}
