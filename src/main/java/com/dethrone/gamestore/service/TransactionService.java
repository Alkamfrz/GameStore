/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.service;

import com.dethrone.gamestore.HibernateUtil;
import com.dethrone.gamestore.model.Transaction;
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
public class TransactionService {

    private static final String QUERY_BY_USER_ID = "from Transaction where user_id = :value";
    private static final String QUERY_ALL_TRANSACTIONS = "from Transaction";

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

    public Transaction createTransaction(Transaction transaction) {
        executeInTransaction(session -> session.persist(transaction));
        return transaction;
    }

    public Optional<Transaction> getTransactionById(UUID transaction_id) {
        return executeQuery(session -> {
            List<Transaction> transactions = session.createQuery(QUERY_BY_USER_ID, Transaction.class).setParameter("value", transaction_id).getResultList();
            if (transactions.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(transactions.get(0));
            }
        });
    }

    public List<Transaction> getAllTransactions() {
        return executeQuery(session -> session.createQuery(QUERY_ALL_TRANSACTIONS, Transaction.class).getResultList());
    }

    public List<Transaction> getTransactionsByUserId(UUID user_id) {
        return executeQuery(session -> session.createQuery(QUERY_BY_USER_ID, Transaction.class).setParameter("value", user_id).getResultList());
    }

    public void updateTransaction(Transaction transaction) {
        executeInTransaction(session -> session.merge(transaction));
    }

    public void deleteTransaction(Transaction transaction) {
        executeInTransaction(session -> session.remove(transaction));
    }
}