package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.service.SecurityService;
import com.dethrone.gamestore.service.UserService;
import com.dethrone.gamestore.service.TransactionService;
import com.dethrone.gamestore.service.GameService;
import com.dethrone.gamestore.service.GenreService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();

        SecurityService securityService = new SecurityService();
        UserService userService = new UserService(securityService);
        TransactionService transactionService = new TransactionService();
        GameService gameService = new GameService();
        GenreService genreService = new GenreService();

        userService.nullifyAllProfilePhotos(ctx);

        ctx.setAttribute("securityService", securityService);
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("transactionService", transactionService);
        ctx.setAttribute("gameService", gameService);
        ctx.setAttribute("genreService", genreService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        HibernateUtil.shutdown();
    }
}