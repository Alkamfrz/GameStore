package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Game;
import com.dethrone.gamestore.model.Publisher;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.GameService;
import com.dethrone.gamestore.service.PublisherService;
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
import java.util.stream.Collectors;

import java.sql.Date;

@WebServlet(name = "GameServlet", urlPatterns = { "/admin/game/*" })
public class GameServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServlet.class);

    private GameService gameService;
    private PublisherService publisherService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        gameService = (GameService) context.getAttribute("gameService");
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
                    UUID gameId = UUID.fromString(pathInfo.substring(1));
                    Optional<Game> gameOpt = gameService.getGameById(gameId);
                    if (gameOpt.isPresent()) {
                        Game game = gameOpt.get();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject gameJson = new JsonObject();
                        gameJson.addProperty("name", game.getGame_name());
                        gameJson.addProperty("release_date", game.getGame_release_date().toString());
                        gameJson.addProperty("rating", game.getGame_rating());
                        gameJson.addProperty("price", game.getGame_price());
                        gameJson.addProperty("description", game.getGame_description());
                        gameJson.addProperty("image", game.getGame_image());

                        response.getWriter().write(gameJson.toString());
                    } else {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");

                        response.getWriter().write(errorJson.toString());
                    }
                } else {
                    setUserAttribute(request, currentUser);
                    setGameData(request);
                    request.getRequestDispatcher(Constants.GAME_VIEW).forward(request, response);
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
                    String gameName = (String) data.get("name");
                    Date gameReleaseDate = new Date((Long) data.get("release_date"));
                    Double gameRating = (Double) data.get("rating");
                    Double gamePrice = (Double) data.get("price");
                    String gameDescription = (String) data.get("description");
                    String gameImage = (String) data.get("image");
                    UUID publisherId = UUID.fromString((String) data.get("publisher_id"));

                    if (gameService.getGameByName(gameName).isPresent()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Game with this name already exists");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }

                    Game newGame = new Game();
                    newGame.setGame_name(gameName);
                    newGame.setGame_release_date(gameReleaseDate);
                    newGame.setGame_rating(gameRating);
                    newGame.setGame_price(gamePrice);
                    newGame.setGame_description(gameDescription);
                    newGame.setGame_image(gameImage);
                    newGame.setPublisher(publisherService.getPublisherById(publisherId).orElse(null));

                    gameService.createGame(newGame);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    JsonObject successJson = new JsonObject();
                    successJson.addProperty("status", "success");
                    successJson.addProperty("message", "Game added successfully");

                    response.getWriter().write(successJson.toString());
                } else if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    String gameIdStr = (String) data.get("game_id");
                    if (gameIdStr == null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", "Game ID is missing");

                        response.getWriter().write(errorJson.toString());
                        return;
                    }
                    UUID gameId = UUID.fromString(gameIdStr);
                    try {
                        if (pathInfo.equals("/edit")) {
                            String newGameName = (String) data.get("name");
                            Date newGameReleaseDate = new Date((Long) data.get("release_date"));
                            Double newGameRating = (Double) data.get("rating");
                            Double newGamePrice = (Double) data.get("price");
                            String newGameDescription = (String) data.get("description");
                            String newGameImage = (String) data.get("image");
                            UUID newPublisherId = UUID.fromString((String) data.get("publisher_id"));

                            Optional<Game> gameToEditOpt = gameService.getGameById(gameId);
                            if (gameToEditOpt.isPresent()) {
                                Game gameToEdit = gameToEditOpt.get();

                                gameToEdit.setGame_name(newGameName);
                                gameToEdit.setGame_release_date(newGameReleaseDate);
                                gameToEdit.setGame_rating(newGameRating);
                                gameToEdit.setGame_price(newGamePrice);
                                gameToEdit.setGame_description(newGameDescription);
                                gameToEdit.setGame_image(newGameImage);
                                gameToEdit.setPublisher(publisherService.getPublisherById(newPublisherId).orElse(null));

                                gameService.updateGame(gameToEdit);

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
                                errorJson.addProperty("message", "Game not found");

                                response.getWriter().write(errorJson.toString());
                            }
                        } else if (pathInfo.equals("/delete")) {
                            Optional<Game> gameToDeleteOpt = gameService.getGameById(gameId);
                            if (gameToDeleteOpt.isPresent()) {
                                Game gameToDelete = gameToDeleteOpt.get();

                                gameService.deleteGame(gameToDelete);

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
                                errorJson.addProperty("message", "Game not found");

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

    private void setGameData(HttpServletRequest request) {
        List<Game> games = gameService.getAllGames();
        List<String> publishers = publisherService.getAllPublishers().stream().map(Publisher::getPublisher_name)
                .collect(Collectors.toList());

        request.setAttribute(Constants.GAMES, games);
        request.setAttribute(Constants.PUBLISHERS, publishers);
    }

    @Override
    public String getServletInfo() {
        return "Genre Servlet";
    }
}
