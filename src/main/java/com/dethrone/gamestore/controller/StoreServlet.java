/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.dethrone.gamestore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Game;
import com.dethrone.gamestore.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.dethrone.gamestore.service.GameService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alkam
 */
@WebServlet(name = "StoreServlet", urlPatterns = { "/store" })
public class StoreServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServlet.class);

    private UserService userService;
    private GameService gameService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        userService = (UserService) context.getAttribute("userService");
        gameService = (GameService) context.getAttribute("gameService");
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

    private void writeResponse(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        response.getWriter().write(json);
    }

    private void setStoreData(HttpServletRequest request) throws SQLException {
        List<Game> games = gameService.getAllGames();
        request.setAttribute("games", games);
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        } else {
            try {
                setStoreData(request);
                request.getRequestDispatcher(Constants.STORE_VIEW).forward(request, response);
            } catch (SQLException e) {
                LOGGER.error(Constants.DATABASE_ERROR, e);
            } catch (Exception e) {
                LOGGER.error(Constants.POST_REQUEST_ERROR, e);
            }
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
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "StoreServlet";
    }
}
