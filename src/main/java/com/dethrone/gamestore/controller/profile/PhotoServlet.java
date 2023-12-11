/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller.profile;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.*;

/**
 *
 * @author alkam
 */
@WebServlet(name = "PhotoServlet", urlPatterns = { "/profile/photo" })
public class PhotoServlet extends HttpServlet {

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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.USER_SESSION_ATTRIBUTE) == null) {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        } else {
            User currentUser = (User) session.getAttribute(Constants.USER_SESSION_ATTRIBUTE);
            String userDirectoryPath = getServletContext().getRealPath(Constants.USER_DIRECTORY)
                    + currentUser.getUser_id().toString().replace("-", "").substring(0, 10) + "/images/";
            Path userDirectory = Paths.get(userDirectoryPath);

            if (currentUser.getProfilePhoto() != null) {
                Path oldPhotoPath = userDirectory.resolve(currentUser.getProfilePhoto());
                Files.deleteIfExists(oldPhotoPath);
                currentUser.setProfilePhoto(null);
                userService.updateUser(currentUser);
                session.setAttribute(Constants.USER_SESSION_ATTRIBUTE, currentUser);
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\": \"failure\"}");
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "DeletePhotoServlet";
    }

}
