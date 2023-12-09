package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.service.SecurityService;
import com.dethrone.gamestore.service.UserService;

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

        // store the services in the servlet context
        ctx.setAttribute("securityService", securityService);
        ctx.setAttribute("userService", userService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // cleanup code if necessary
    }
}