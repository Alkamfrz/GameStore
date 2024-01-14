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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser != null) {
            redirectToPageBasedOnRole(sessionUser, request, response);
        } else {
            processRequest(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User sessionUser = (User) request.getSession().getAttribute("user");
            if (sessionUser != null) {
                redirectToPageBasedOnRole(sessionUser, request, response);
                return;
            }

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

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
