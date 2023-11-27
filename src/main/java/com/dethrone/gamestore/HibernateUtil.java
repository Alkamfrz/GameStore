/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dethrone.gamestore.model.User;

import java.util.Optional;

/**
 *
 * @author alkam
 */
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static StandardServiceRegistry registry;

    private static class SessionFactoryHolder {
        static final SessionFactory sessionFactory;

        static {
            try {
                Configuration configuration = new Configuration();
                configuration.addAnnotatedClass(User.class);
                configuration.configure();
                registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(registry);
            } catch (HibernateException ex) {
                logger.error("Initial SessionFactory creation failed.", ex);
                Optional.ofNullable(registry).ifPresent(StandardServiceRegistryBuilder::destroy);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }

    private HibernateUtil() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static SessionFactory getSessionFactory() {
        return SessionFactoryHolder.sessionFactory;
    }

    public static void shutdown() {
        Optional.ofNullable(registry).ifPresent(StandardServiceRegistryBuilder::destroy);
        if (SessionFactoryHolder.sessionFactory != null && !SessionFactoryHolder.sessionFactory.isClosed()) {
            SessionFactoryHolder.sessionFactory.close();
        }
    }
}