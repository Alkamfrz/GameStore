package com.dethrone.gamestore.controller;

import com.dethrone.gamestore.Constants;
import com.dethrone.gamestore.model.Game;
import com.dethrone.gamestore.model.Genre;
import com.dethrone.gamestore.model.Publisher;
import com.dethrone.gamestore.model.User;
import com.dethrone.gamestore.service.GameService;
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
import java.util.*;

@WebServlet(name = "GameServlet", urlPatterns = { "/admin/games/*" })
public class GameServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServlet.class);

    private GameService gameService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext context = getServletContext();
        gameService = (GameService) context.getAttribute("gameService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Similar logic as in UserServlet for handling GET requests
        // Adjust the logic according to your requirements for game management
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (isUserLoggedIn(session)) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.getRole().equals(User.Role.ADMIN)) {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null && pathInfo.equals("/add")) {
                    BufferedReader reader = request.getReader();
                    Gson gson = new Gson();
                    Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    String gameName = (String) data.get("game_name");
                    String priceStr = (String) data.get("price");
                    String description = (String) data.get("description");
                    String image = (String) data.get("image");
                    String genreIdStr = (String) data.get("genre_id");
                    String publisherIdStr = (String) data.get("publisher_id");

                    List<String> errorMessage = validateGameData(gameName, priceStr, description, image, genreIdStr, publisherIdStr);

                    if (errorMessage != null && !errorMessage.isEmpty()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject errorJson = new JsonObject();
                        errorJson.addProperty("status", "failure");
                        errorJson.addProperty("message", String.join(", ", errorMessage));

                        response.getWriter().write(errorJson.toString());
                    } else {
                        int price = Integer.parseInt(priceStr);
                        UUID genreId = UUID.fromString(genreIdStr);
                        UUID publisherId = UUID.fromString(publisherIdStr);

                        Game game = new Game();
                        game.setGame_name(gameName);
                        game.setPrice(price);
                        game.setDescription(description);
                        game.setImage(image);

                        Genre genre = gameService.getGenreById(genreId).orElse(null);
                        Publisher publisher = gameService.getPublisherById(publisherId).orElse(null);

                        game.setGenre(genre);
                        game.setPublisher(publisher);

                        gameService.createGame(game);

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        JsonObject successJson = new JsonObject();
                        successJson.addProperty("status", "success");
                        successJson.addProperty("message", Constants.REGISTRATION_SUCCESS);

                        response.getWriter().write(successJson.toString());
                    }
                }
                if (pathInfo != null && (pathInfo.equals("/edit") || pathInfo.equals("/delete"))) {
                    BufferedReader reader = request.getReader();
                    Gson gson = new Gson();
                    Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                    }.getType());

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
                            // Extract the new game data from the request
                            // ...

                            Optional<Game> gameToEditOpt = gameService.getGameById(gameId);
                            if (gameToEditOpt.isPresent()) {
                                Game gameToEdit = gameToEditOpt.get();

                                // Update the game's fields with the new data
                                // ...

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

    private List<String> validateGameData(String gameName, String priceStr, String description, String image,
            String genreIdStr, String publisherIdStr) {
        List<String> errorMessages = new ArrayList<>();
        if (gameName.isEmpty() || priceStr.isEmpty() || description.isEmpty() || image.isEmpty() || genreIdStr.isEmpty() || publisherIdStr.isEmpty()) {
            // Implement validation logic for game data
            // Add appropriate error messages to a list if there are validation issues
            // Return the list of error messages
            // Example: Check if fields are not empty, if numeric fields contain valid numbers, etc.
        }
        return errorMessages;
    }

    private boolean isUserLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }

    // ... (other methods)

    @Override
    public String getServletInfo() {
        return "GameServlet";
    }
}
