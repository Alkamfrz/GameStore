/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.util.List;

import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.SecurityService;
import com.dethrone.gamestore.service.UserService;

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
@WebServlet(name = "DashboardServlet", urlPatterns = { "/admin/dashboard" })
public class DashboardServlet extends HttpServlet {

    private static final String LOGIN_URL = "/login";
    private static final String USERNAME_ATTRIBUTE = "username";
    private static final String USERS_ATTRIBUTE = "users";
    private static final String TOTAL_USERS_ATTRIBUTE = "totalUsers";
    private static final String NEW_USERS_THIS_MONTH_ATTRIBUTE = "newUsersThisMonth";
    private static final String PROFILE_PHOTO_ATTRIBUTE = "profilePhoto";
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private UserService userService;

    public DashboardServlet() {
        SecurityService securityService = new SecurityService();
        this.userService = new UserService(securityService);
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + LOGIN_URL);
        } else {
            try {
                User currentUser = (User) session.getAttribute("user");
                String username = currentUser.getUsername();
                String profilePhoto = currentUser.getProfilePhoto();
                int id = currentUser.getId();

                request.setAttribute(USERNAME_ATTRIBUTE, username);
                request.setAttribute(PROFILE_PHOTO_ATTRIBUTE, profilePhoto);
                request.setAttribute("id", id);

                List<User> users = userService.getAllUsers();
                users.sort((User u1, User u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()));
                request.setAttribute(USERS_ATTRIBUTE, users);

                int totalUsers = userService.getTotalUsers();
                int newUsersThisMonth = userService.getNewUsersThisMonth();
                request.setAttribute(TOTAL_USERS_ATTRIBUTE, totalUsers);
                request.setAttribute(NEW_USERS_THIS_MONTH_ATTRIBUTE, newUsersThisMonth);

                request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
            } catch (Exception e) {
                LOGGER.error("Error processing request", e);
            }
        }
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
        processRequest(request, response);
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
