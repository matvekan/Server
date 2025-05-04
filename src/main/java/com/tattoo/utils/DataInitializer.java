package com.tattoo.utils;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Role;
import com.tattoo.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DataInitializer {
    public static void initialize() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Проверяем, есть ли уже данные
            if(session.createQuery("FROM Role", Role.class).list().isEmpty()) {
                // Создаем роли
                Role adminRole = new Role("ADMIN");
                Role masterRole = new Role("MASTER");
                Role clientRole = new Role("CLIENT");

                session.save(adminRole);
                session.save(masterRole);
                session.save(clientRole);

                // Создаем пользователей
                User admin = new User("admin", "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6",
                        adminRole, "admin@tattoo.com");
                User master = new User("master1", "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6",
                        masterRole, "master@tattoo.com");
                User client = new User("client1", "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6",
                        clientRole, "client@tattoo.com");

                session.save(admin);
                session.save(master);
                session.save(client);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}