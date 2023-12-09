/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.dethrone.gamestore.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alkam
 */
@WebFilter(filterName = "RoleBasedAccessControlFilter", urlPatterns = { "/*" })
public class RoleBasedAccessControlFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with. If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public RoleBasedAccessControlFilter() {
    }

    /**
     *
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     *
     * @exception IOException      if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + Constants.LOGIN_URL;
        String registerURI = request.getContextPath() + Constants.REGISTER_URL;

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        boolean registerRequest = request.getRequestURI().equals(registerURI);
        boolean isStaticResource = request.getRequestURI().matches(".+\\.(css|jpg|png|gif|js)");

        if (loggedIn) {
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    String role = user.getRole().name().toLowerCase();
                    String path = request.getRequestURI().substring(request.getContextPath().length());

                    if (path.startsWith("/admin") && !"admin".equals(role)) {
                        response.sendRedirect(request.getContextPath() + "/store");
                    } else {
                        chain.doFilter(request, response);
                    }
                } else {
                    response.sendRedirect(loginURI);
                }
            } else {
                // Handle the case where session is null
            }
        } else if (loginRequest || registerRequest || isStaticResource) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("RoleBasedAccessControlFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("RoleBasedAccessControlFilter()");
        }
        StringBuffer sb = new StringBuffer("RoleBasedAccessControlFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
