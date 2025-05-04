package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Role;
import com.tattoo.models.User;
import com.tattoo.utils.PasswordHasher;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

public class UserService {
    public static boolean registerUser(String login, String password, String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            if (session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult() != null) {
                return false; // Пользователь уже существует
            }

            User user = new User();
            user.setLogin(login);
            user.setPasswordHash(PasswordHasher.hash(password));
            user.setEmail(email);
            user.setRegistrationDate(new Date());

            // По умолчанию роль "CLIENT"
            user.setRole(session.createQuery("FROM Role WHERE name = 'CLIENT'", Role.class)
                    .uniqueResult());

            session.save(user);
            tx.commit();
            return true;
        }
    }

    public static boolean updateUserProfile(Long userId, String fullName, String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                user.setFullName(fullName);
                user.setPhone(phone);
                session.update(user);
                tx.commit();
                return true;
            }
            return false;
        }
    }
}