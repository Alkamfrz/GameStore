package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.Genre;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.UUID;

public class GenreService {

    private static final String QUERY_BY_GENRE_ID = "from Genre where genre_id = :value";
    private static final String QUERY_ALL_GENRES = "from Genre";
    private static final String QUERY_BY_GENRE_NAME = "from Genre where genre_name = :value";

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

    public Genre createGenre(Genre genre) {
        executeInTransaction(session -> session.persist(genre));
        return genre;
    }

    public Optional<Genre> getGenreById(UUID genre_id) {
        return executeQuery(session -> {
            List<Genre> genres = session.createQuery(QUERY_BY_GENRE_ID, Genre.class)
                    .setParameter("value", genre_id).getResultList();
            if (genres.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(genres.get(0));
            }
        });
    }

    public Optional<Genre> getGenreByName(String genre_name) {
        return executeQuery(session -> {
            List<Genre> genres = session.createQuery(QUERY_BY_GENRE_NAME, Genre.class)
                    .setParameter("value", genre_name).getResultList();
            if (genres.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(genres.get(0));
            }
        });
    }

    public List<Genre> getAllGenres() {
        return executeQuery(session -> session.createQuery(QUERY_ALL_GENRES, Genre.class).getResultList());
    }

    public void updateGenre(Genre genre) {
        executeInTransaction(session -> session.merge(genre));
    }

    public void deleteGenre(Genre genre) {
        executeInTransaction(session -> session.remove(genre));
    }
}