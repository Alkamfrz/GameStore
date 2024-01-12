package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Genre;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.GenreService;
import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet(name = "GenreServlet", urlPatterns = { "/admin/genres/*" })
public class GenreServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreServlet.class);

    private GenreService genreService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        genreService = (GenreService) context.getAttribute("genreService");
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
                    UUID genreId = UUID.fromString(pathInfo.substring(1));
                    Optional<Genre> genreOpt = genreService.getGenreById(genreId);
                    if (genreOpt.isPresent()) {
                        Genre genre = genreOpt.get();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject genreJson = new JsonObject();
                        genreJson.addProperty("name", genre.getGenre_name());
                        genreJson.addProperty("description", genre.getGenre_description());

                        response.getWriter().write(genreJson.toString());
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");

                        response.getWriter().write(errorJson.toString());
                    }
                } else {
                    setUserAttribute(request, currentUser);
                    setGenreData(request);
                    request.getRequestDispatcher(Constants.GENRE_VIEW).forward(request, response);
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
                    String genreName = ((String) data.get("name")).toLowerCase();
                    String genreDescription = (String) data.get("description");
                    genreName = Arrays.stream(genreName.split(" "))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                            .collect(Collectors.joining(" "));

                    if (genreService.getGenreByName(genreName).isPresent()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Genre with this name already exists");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }

                    Genre newGenre = new Genre();
                    newGenre.setGenre_name(genreName);
                    newGenre.setGenre_description(genreDescription);
                    genreService.createGenre(newGenre);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    JsonObject successJson = new JsonObject();
                    successJson.addProperty("status", "success");
                    successJson.addProperty("message", "Genre added successfully");

                    response.getWriter().write(successJson.toString());
                } else if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    String genreIdStr = (String) data.get("genre_id");
                    if (genreIdStr == null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Genre ID is missing");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }
                    UUID genreId = UUID.fromString(genreIdStr);
                    try {
                        if (pathInfo.equals("/edit")) {
                            String newGenreName = ((String) data.get("name")).toLowerCase();
                            String newGenreDescription = (String) data.get("description");
                            newGenreName = Arrays.stream(newGenreName.split(" "))
                                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                                    .collect(Collectors.joining(" "));

                            Optional<Genre> genreToEditOpt = genreService.getGenreById(genreId);
                            if (genreToEditOpt.isPresent()) {
                                Genre genreToEdit = genreToEditOpt.get();

                                genreToEdit.setGenre_name(newGenreName);
                                genreToEdit.setGenre_description(newGenreDescription);

                                genreService.updateGenre(genreToEdit);

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
                                errorJson.addProperty("message", "Genre not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        } else if (pathInfo.equals("/delete")) {
                            Optional<Genre> genreToDeleteOpt = genreService.getGenreById(genreId);
                            if (genreToDeleteOpt.isPresent()) {
                                Genre genreToDelete = genreToDeleteOpt.get();

                                genreService.deleteGenre(genreToDelete);

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
                                errorJson.addProperty("message", "Genre not found");

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
        List<Genre> genres = genreService.getAllGenres();
        String role = currentUser.getRole().toString().toLowerCase();

        request.setAttribute(Constants.USERNAME, username);
        request.setAttribute(Constants.GENRES, genres);
        request.setAttribute(Constants.ROLE, role);
    }

    private void setGenreData(HttpServletRequest request) throws ServletException {
        List<Genre> genres = genreService.getAllGenres();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Genre genre : genres) {
            String[] genreData = parameterMap.get(genre.getGenre_id().toString());
            if (genreData != null) {
                genre.setGenre_name(genreData[0]);
            }
        }
        request.setAttribute(Constants.GENRES, genres);
    }

    @Override
    public String getServletInfo() {
        return "GenreServlet";
    }
}
