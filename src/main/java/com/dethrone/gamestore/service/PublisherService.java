package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.Publisher;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.UUID;

public class PublisherService {

    private static final String QUERY_BY_PUBLISHER_ID = "from Publisher where publisher_id = :value";
    private static final String QUERY_ALL_PUBLISHERS = "from Publisher";
    private static final String QUERY_BY_PUBLISHER_NAME = "from Publisher where publisher_name = :value";

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

    public Publisher createPublisher(Publisher publisher) {
        executeInTransaction(session -> session.persist(publisher));
        return publisher;
    }

    public Optional<Publisher> getPublisherById(UUID publisher_id) {
        return executeQuery(session -> {
            List<Publisher> publishers = session.createQuery(QUERY_BY_PUBLISHER_ID, Publisher.class)
                    .setParameter("value", publisher_id).getResultList();
            if (publishers.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(publishers.get(0));
            }
        });
    }

    public Optional<Publisher> getPublisherByName(String publisher_name) {
        return executeQuery(session -> {
            List<Publisher> publishers = session.createQuery(QUERY_BY_PUBLISHER_NAME, Publisher.class)
                    .setParameter("value", publisher_name).getResultList();
            if (publishers.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(publishers.get(0));
            }
        });
    }

    public List<Publisher> getAllPublishers() {
        return executeQuery(session -> session.createQuery(QUERY_ALL_PUBLISHERS, Publisher.class).getResultList());
    }

    public void updatePublisher(Publisher publisher) {
        executeInTransaction(session -> session.merge(publisher));
    }

    public void deletePublisher(Publisher publisher) {
        executeInTransaction(session -> session.remove(publisher));
    }
}