/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author alkam
 */
@WebServlet(name = "ProfileServlet", urlPatterns = { "/profile/*" })
@MultipartConfig
public class ProfileServlet extends HttpServlet {

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
        if (session == null || session.getAttribute(Constants.USER_SESSION_ATTRIBUTE) == null) {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        } else {
            User currentUser = (User) session.getAttribute(Constants.USER_SESSION_ATTRIBUTE);
            Optional<User> optionalUser = userService.getUserById(currentUser.getUser_id());
            if (optionalUser.isPresent()) {
                currentUser = optionalUser.get();
            } else {
                response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
                return;
            }

            setUserAttributes(request, currentUser);

            request.getRequestDispatcher(Constants.PROFILE_VIEW).forward(request, response);
        }
    }

    private void setUserAttributes(HttpServletRequest request, User currentUser) {
        request.setAttribute(Constants.USER_ID, currentUser.getUser_id());
        request.setAttribute(Constants.FIRST_NAME, currentUser.getFirstName());
        request.setAttribute(Constants.LAST_NAME, currentUser.getLastName());
        request.setAttribute(Constants.EMAIL, currentUser.getEmail());
        request.setAttribute(Constants.ROLE, currentUser.getRole().toString().toLowerCase());
        request.setAttribute(Constants.USERNAME, currentUser.getUsername());
        request.setAttribute(Constants.PROFILE_PHOTO, currentUser.getProfilePhoto());
        request.setAttribute(Constants.CREATED_AT,
                currentUser.getCreatedAt().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        request.setAttribute(Constants.LAST_LOGIN,
                currentUser.getLastLogin().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
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
        if (session == null || session.getAttribute(Constants.USER_SESSION_ATTRIBUTE) == null) {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        } else {
            String action = request.getParameter("action");
            if ("changePassword".equals(action)) {
                handleChangePassword(request, response, session);
            } else if ("updateProfile".equals(action)) {
                handleUpdateProfile(request, response, session);
            }
        }
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        User currentUser = (User) session.getAttribute(Constants.USER_SESSION_ATTRIBUTE);

        String oldPassword = Optional.ofNullable(request.getParameter(Constants.CURRENT_PASSWORD))
                .orElseThrow(() -> new ServletException(Constants.CURRENT_PASSWORD_REQUIRED));
        String newPassword = Optional.ofNullable(request.getParameter(Constants.NEW_PASSWORD))
                .orElseThrow(() -> new ServletException(Constants.NEW_PASSWORD_REQUIRED));
        String confirmNewPassword = Optional.ofNullable(request.getParameter(Constants.CONFIRM_PASSWORD))
                .orElseThrow(() -> new ServletException(Constants.CONFIRM_PASSWORD_REQUIRED));

        List<String> errorMessage = validatePasswordChange(oldPassword, newPassword, confirmNewPassword, currentUser);
        if (!errorMessage.isEmpty()) {
            request.setAttribute(Constants.ERROR_MESSAGE, errorMessage);
            request.getRequestDispatcher(Constants.PROFILE_VIEW).forward(request, response);
        } else {
            try {
                userService.changePassword(currentUser, oldPassword, newPassword);
                session.setAttribute(Constants.USER_SESSION_ATTRIBUTE, currentUser);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\": \"success\"}");
            } catch (IllegalArgumentException e) {
                errorMessage.add(e.getMessage());
                request.setAttribute(Constants.ERROR_MESSAGE, errorMessage);
                request.getRequestDispatcher(Constants.PROFILE_VIEW).forward(request, response);
            }
        }
    }

    private List<String> validatePasswordChange(String oldPassword, String newPassword, String confirmNewPassword,
            User currentUser) {
        List<String> errorMessages = new ArrayList<>();
        if (!userService.isPasswordMatch(oldPassword, currentUser)) {
            errorMessages.add(Constants.CURRENT_PASSWORD_MISMATCH);
        }
        if (StringUtils.isBlank(newPassword)) {
            errorMessages.add(Constants.NEW_PASSWORD_REQUIRED);
        }
        if (!newPassword.equals(confirmNewPassword)) {
            errorMessages.add(Constants.PASSWORD_MISMATCH);
        }
        return errorMessages;
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        User currentUser = (User) session.getAttribute(Constants.USER_SESSION_ATTRIBUTE);

        String firstName = Optional.ofNullable(request.getParameter(Constants.FIRST_NAME))
                .orElseThrow(() -> new ServletException(Constants.FIRST_NAME_REQUIRED));
        String lastName = Optional.ofNullable(request.getParameter(Constants.LAST_NAME))
                .orElseThrow(() -> new ServletException(Constants.LAST_NAME_REQUIRED));

        List<String> errorMessage = validateFormData(firstName, lastName);
        if (!errorMessage.isEmpty()) {
            request.setAttribute(Constants.ERROR_MESSAGE, errorMessage);
            request.getRequestDispatcher(Constants.PROFILE_VIEW).forward(request, response);
        } else {
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);

            Part profilePhotoPart = request.getPart(Constants.PHOTO);
            if (profilePhotoPart != null && profilePhotoPart.getSize() > 0) {
                String fileName = UUID.randomUUID().toString() + "." +
                        com.google.common.io.Files.getFileExtension(profilePhotoPart.getSubmittedFileName());
                try (InputStream fileContent = profilePhotoPart.getInputStream()) {
                    String userDirectoryPath = getServletContext().getRealPath(Constants.USER_DIRECTORY)
                            + currentUser.getUser_id().toString().replace("-", "").substring(0, 10)
                            + Constants.USER_IMAGE_DIRECTORY;
                    Path userDirectory = Paths.get(userDirectoryPath);
                    Files.createDirectories(userDirectory);

                    if (currentUser.getProfilePhoto() != null) {
                        Path oldPhotoPath = userDirectory.resolve(currentUser.getProfilePhoto());
                        Files.deleteIfExists(oldPhotoPath);
                    }

                    Files.copy(fileContent, userDirectory.resolve(fileName),
                            StandardCopyOption.REPLACE_EXISTING);
                    currentUser.setProfilePhoto(fileName);
                }
            } else if (request.getParameter("deletePhoto") != null) {
                String userDirectoryPath = getServletContext().getRealPath(Constants.USER_DIRECTORY)
                        + currentUser.getUser_id().toString().replace("-", "").substring(0, 10) + "/images/";
                Path userDirectory = Paths.get(userDirectoryPath);

                if (currentUser.getProfilePhoto() != null) {
                    Path oldPhotoPath = userDirectory.resolve(currentUser.getProfilePhoto());
                    Files.deleteIfExists(oldPhotoPath);
                    currentUser.setProfilePhoto(null);
                }
            }

            userService.updateUser(currentUser);
            session.setAttribute(Constants.USER_SESSION_ATTRIBUTE, currentUser);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\": \"success\"}");
        }
    }

    private List<String> validateFormData(String firstName, String lastName) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(firstName)) {
            errorMessages.add(Constants.FIRST_NAME_REQUIRED);
        }
        if (StringUtils.isBlank(lastName)) {
            errorMessages.add(Constants.LAST_NAME_REQUIRED);
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
        return "ProfileServlet";
    }

}
