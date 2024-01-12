package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Transaction;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.TransactionService;
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

@WebServlet(name = "DashboardServlet", urlPatterns = { "/admin/dashboard" })
public class DashboardServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

    private UserService userService;
    private TransactionService transactionService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        userService = (UserService) context.getAttribute("userService");
        transactionService = (TransactionService) context.getAttribute("transactionService");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!isUserLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        } else {
            try {
                User currentUser = (User) session.getAttribute("user");
                setUserAttributes(request, currentUser);
                setDashboardData(request);

                request.getRequestDispatcher(Constants.DASHBOARD_VIEW).forward(request, response);
            } catch (SQLException e) {
                LOGGER.error(Constants.DATABASE_ERROR, e);
            } catch (Exception e) {
                LOGGER.error(Constants.POST_REQUEST_ERROR, e);
            }
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

    private void setDashboardData(HttpServletRequest request) throws SQLException {
        List<User> users = userService.getAllUsers();
        users.sort((User u1, User u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()));
        request.setAttribute(Constants.USERS, users);

        int totalUsers = userService.getTotalUsers();
        int newUsersThisMonth = userService.getNewUsersThisMonth();
        request.setAttribute(Constants.TOTAL_USERS, totalUsers);
        request.setAttribute(Constants.NEW_USERS_THIS_MONTH, newUsersThisMonth);

        List<Transaction> transactions = transactionService.getAllTransactions();
        request.setAttribute(Constants.TRANSACTIONS, transactions);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "DashboardServlet";
    }

}
