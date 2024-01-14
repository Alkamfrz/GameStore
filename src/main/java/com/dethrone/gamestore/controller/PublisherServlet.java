package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Publisher;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.PublisherService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "PublisherServlet", urlPatterns = { "/admin/publisher/*" })
public class PublisherServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherServlet.class);

    private PublisherService publisherService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        publisherService = (PublisherService) context.getAttribute("publisherService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (isUserLoggedIn(session)) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.getRole().equals(User.Role.ADMIN)) {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    if (pathInfo.equals("/all")) {
                        List<Publisher> publishers = publisherService.getAllPublishers();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonArray publishersJson = new JsonArray();
                        for (Publisher publisher : publishers) {
                            JsonObject publisherJson = new JsonObject();
                            publisherJson.addProperty("id", publisher.getPublisher_id().toString());
                            publisherJson.addProperty("name", publisher.getPublisher_name());
                            publishersJson.add(publisherJson);
                        }

                        response.getWriter().write(publishersJson.toString());
                    } else {
                        UUID publisherId = UUID.fromString(pathInfo.substring(1));
                        Optional<Publisher> publisherOpt = publisherService.getPublisherById(publisherId);
                        if (publisherOpt.isPresent()) {
                            Publisher publisher = publisherOpt.get();
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            JsonObject publisherJson = new JsonObject();
                            publisherJson.addProperty("name", publisher.getPublisher_name());
                            publisherJson.addProperty("country", publisher.getPublisher_country());
                            publisherJson.addProperty("city", publisher.getPublisher_city());
                            publisherJson.addProperty("address", publisher.getPublisher_address());
                            publisherJson.addProperty("phone", publisher.getPublisher_phone());
                            publisherJson.addProperty("email", publisher.getPublisher_email());
                            publisherJson.addProperty("website", publisher.getPublisher_website());
                            publisherJson.addProperty("description", publisher.getPublisher_description());

                            response.getWriter().write(publisherJson.toString());
                        } else {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            JsonObject errorJson = new JsonObject();
                            errorJson.addProperty("status", "failure");

                            response.getWriter().write(errorJson.toString());
                        }
                    }
                } else {
                    setUserAttribute(request, currentUser);
                    setPublisherData(request);
                    request.getRequestDispatcher(Constants.PUBLISHER_VIEW).forward(request, response);
                }
            } else {
                response.sendRedirect(request.getContextPath() + Constants.STORE_VIEW);
            }
        } else {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (isUserLoggedIn(session)) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.getRole().equals(User.Role.ADMIN)) {
                String pathInfo = request.getPathInfo();
                BufferedReader reader = request.getReader();
                Gson gson = new Gson();
                Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                }.getType());

                if (pathInfo != null && pathInfo.equals("/add")) {
                    String publisherName = (String) data.get("name");
                    String publisherCountry = (String) data.get("country");
                    String publisherCity = (String) data.get("city");
                    String publisheraddress = (String) data.get("address");
                    String publisherPhone = (String) data.get("phone");
                    String publisherEmail = (String) data.get("email");
                    String publisherWebsite = (String) data.get("website");
                    String publisherDescription = (String) data.get("description");

                    if (publisherService.getPublisherByName(publisherName).isPresent()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Publisher with this name already exists");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }

                    Publisher newPublisher = new Publisher();
                    newPublisher.setPublisher_name(publisherName);
                    newPublisher.setPublisher_country(publisherCountry);
                    newPublisher.setPublisher_city(publisherCity);
                    newPublisher.setPublisher_address(publisheraddress);
                    newPublisher.setPublisher_phone(publisherPhone);
                    newPublisher.setPublisher_email(publisherEmail);
                    newPublisher.setPublisher_website(publisherWebsite);
                    newPublisher.setPublisher_description(publisherDescription);
                    publisherService.createPublisher(newPublisher);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    JsonObject successJson = new JsonObject();
                    successJson.addProperty("status", "success");
                    successJson.addProperty("message", "Publisher added successfully");

                    response.getWriter().write(successJson.toString());
                } else if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    String publisherIdStr = (String) data.get("publisher_id");
                    if (publisherIdStr == null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Publisher ID is missing");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }
                    UUID publisherId = UUID.fromString(publisherIdStr);
                    try {
                        if (pathInfo.equals("/edit")) {
                            String newPublisherName = (String) data.get("name");
                            String newPublisherCountry = (String) data.get("country");
                            String newPublisherCity = (String) data.get("city");
                            String newPublisheraddress = (String) data.get("address");
                            String newPublisherPhone = (String) data.get("phone");
                            String newPublisherEmail = (String) data.get("email");
                            String newPublisherWebsite = (String) data.get("website");
                            String newPublisherDescription = (String) data.get("description");

                            Optional<Publisher> publisherToEditOpt = publisherService.getPublisherById(publisherId);
                            if (publisherToEditOpt.isPresent()) {
                                Publisher publisherToEdit = publisherToEditOpt.get();

                                publisherToEdit.setPublisher_name(newPublisherName);
                                publisherToEdit.setPublisher_country(newPublisherCountry);
                                publisherToEdit.setPublisher_city(newPublisherCity);
                                publisherToEdit.setPublisher_address(newPublisheraddress);
                                publisherToEdit.setPublisher_phone(newPublisherPhone);
                                publisherToEdit.setPublisher_email(newPublisherEmail);
                                publisherToEdit.setPublisher_website(newPublisherWebsite);
                                publisherToEdit.setPublisher_description(newPublisherDescription);

                                publisherService.updatePublisher(publisherToEdit);

                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject successJson = new JsonObject();
                                successJson.addProperty("status", "success");

                                response.getWriter().write(successJson.toString());
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject errorJson = new JsonObject();
                                errorJson.addProperty("status", "failure");
                                errorJson.addProperty("message", "Publisher not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        } else if (pathInfo.equals("/delete")) {
                            Optional<Publisher> publisherToDeleteOpt = publisherService.getPublisherById(publisherId);
                            if (publisherToDeleteOpt.isPresent()) {
                                Publisher publisherToDelete = publisherToDeleteOpt.get();

                                publisherService.deletePublisher(publisherToDelete);

                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject successJson = new JsonObject();
                                successJson.addProperty("status", "success");

                                response.getWriter().write(successJson.toString());
                            } else {
                                response.setContentType("application/json");
                                response.setCharacterEncoding("UTF-8");

                                JsonObject errorJson = new JsonObject();
                                errorJson.addProperty("status", "failure");
                                errorJson.addProperty("message", "Publisher not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error(Constants.DATABASE_ERROR, e);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Database error: " + e.getMessage());

                        response.getWriter().write(errorJson.toString());
                    }
                }
            } else {
                response.sendRedirect(request.getContextPath() + Constants.STORE_VIEW);
            }
        } else {
            response.sendRedirect(request.getContextPath() + Constants.LOGIN_URL);
        }
    }

    private boolean isUserLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    private void setUserAttribute(HttpServletRequest request, User currentUser) {
        String username = currentUser.getUsername();
        String role = currentUser.getRole().toString().toLowerCase();

        request.setAttribute(Constants.USERNAME, username);
        request.setAttribute(Constants.ROLE, role);
    }

    private void setPublisherData(HttpServletRequest request) throws ServletException {
        List<Publisher> publishers = publisherService.getAllPublishers();
        request.setAttribute(Constants.PUBLISHERS, publishers);
    }

    @Override
    public String getServletInfo() {
        return "Publisher Servlet";
    }
}