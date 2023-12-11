package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.service.SecurityService;
import com.dethrone.gamestore.service.UserService;
import com.dethrone.gamestore.service.TransactionService;

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

        userService.nullifyAllProfilePhotos();

        ctx.setAttribute("securityService", securityService);
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("transactionService", transactionService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}