/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.Game;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.UUID;


/**
 *
 * @author alkam
 */
public class GameService {

    private static final String QUERY_BY_GAME_ID = "from Game where game_id = :value";
    private static final String QUERY_ALL_GAMES = "from Game";

    private void executeInTransaction(Consumer<Session> action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                action.accept(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    private <T> T executeQuery(Function<Session, T> query) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return query.apply(session);
        }
    }

    public Game createGame(Game game) {
        executeInTransaction(session -> session.persist(game));
        return game;
    }

    public Optional<Game> getGameById(UUID game_id) {
        return executeQuery(session -> {
            List<Game> games = session.createQuery(QUERY_BY_GAME_ID, Game.class)
                    .setParameter("value", game_id).getResultList();
            if (games.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(games.get(0));
            }
        });
    }

    public List<Game> getAllGames() {
        return executeQuery(session -> session.createQuery(QUERY_ALL_GAMES, Game.class).getResultList());
    }

    public void updateGame(Game game) {
        executeInTransaction(session -> session.merge(game));
    }

    public void deleteGame(Game game) {
        executeInTransaction(session -> session.remove(game));
    }
}
