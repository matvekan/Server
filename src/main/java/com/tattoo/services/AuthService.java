package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.User;
import com.tattoo.utils.PasswordHasher;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class AuthService {
    public static boolean authenticate(String login, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);

            User user = query.uniqueResult();
            return user != null && PasswordHasher.verify(password, user.getPasswordHash());
        }
    }

    public static User getUserByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }
}