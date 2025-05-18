package com.tattoo.services;

import com.tattoo.models.*;
import com.tattoo.network.commands.*;
import com.tattoo.network.responses.*;
import com.tattoo.config.HibernateUtil;
import com.tattoo.utils.PasswordHasher;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class CommandProcessor {

    public Response process(Command command) {
        try {
            switch (command.getType()) {
                case AUTH:
                    return processAuth((AuthCommand) command);

                case REGISTER:
                    return processRegister((RegisterCommand) command);

                case BOOK_APPOINTMENT:
                    return processBooking((BookingCommand) command);

                case MASTER:
                    return processMaster((MasterCommand) command);

                case SERVICE:
                    return processService((ServiceCommand) command);

                default:
                    return new ErrorResponse("Unknown command type: " + command.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse("Server error: " + e.getMessage());
        }
    }

    private Response processRegister(RegisterCommand cmd) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Проверка существования пользователя
            if (isLoginExists(session, cmd.getLogin())) {
                return new RegisterResponse(false, "Логин уже занят", null);
            }

            // Получаем или создаем роль CLIENT
            Role clientRole = getOrCreateClientRole(session);

            // Создаем нового пользователя
            User newUser = createNewUser(cmd, clientRole);
            session.persist(newUser);

            tx.commit();
            return new RegisterResponse(true, "Регистрация успешна", newUser.getLogin());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            return new ErrorResponse("Registration failed: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private boolean isLoginExists(Session session, String login) {
        Query<User> query = session.createQuery(
                "FROM User WHERE login = :login", User.class);
        query.setParameter("login", login);
        return !query.getResultList().isEmpty();
    }

    private Role getOrCreateClientRole(Session session) {
        Query<Role> query = session.createQuery(
                "FROM Role WHERE name = 'CLIENT'", Role.class);
        Role clientRole = query.uniqueResult();

        if (clientRole == null) {
            clientRole = new Role("CLIENT");
            clientRole.setDescription("Роль по умолчанию для клиентов");
            session.persist(clientRole);
        }
        return clientRole;
    }

    private User createNewUser(RegisterCommand cmd, Role role) {
        User newUser = new User();
        newUser.setLogin(cmd.getLogin());
        newUser.setPasswordHash(PasswordHasher.hash(cmd.getPassword()));
        newUser.setEmail(cmd.getEmail());
        newUser.setFullName(cmd.getFullName());
        newUser.setPhone(cmd.getPhone());
        newUser.setRegistrationDate(new Date());
        newUser.setRole(role);
        return newUser;
    }

    private Response processAuth(AuthCommand cmd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User WHERE login = :login", User.class);
            query.setParameter("login", cmd.getLogin());

            User user = query.uniqueResult();

            if (user == null) {
                return new AuthResponse(false, "Пользователь не найден");
            }

            boolean verified = PasswordHasher.verify(cmd.getPassword(), user.getPasswordHash());
            return new AuthResponse(verified, verified ? "Успешная авторизация" : "Неверный пароль");
        } catch (Exception e) {
            return new ErrorResponse("Authentication error: " + e.getMessage());
        }
    }

    private Response processBooking(BookingCommand cmd) {
        // Реализация обработки бронирования
        return new ErrorResponse("Booking not implemented yet");
    }

    private Response processMaster(MasterCommand cmd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            switch (cmd.getAction()) {
                case GET_ALL:
                    List<Master> masters = session.createQuery(
                            "FROM Master", Master.class).list();
                    return new ListResponse<>(true, "Masters retrieved", masters);

                case GET_AVAILABLE_TIME:
                    Query<LocalDateTime> query = session.createQuery(
                            "SELECT a.dateTime FROM Appointment a WHERE a.master.id = :masterId " +
                                    "AND CAST(a.dateTime AS date) = :date", LocalDateTime.class);
                    query.setParameter("masterId", cmd.getMasterId());
                    query.setParameter("date", LocalDate.parse(cmd.getDate()));

                    List<LocalDateTime> bookedSlots = query.getResultList();
                    // Здесь должна быть логика генерации доступных слотов
                    return new ListResponse<>(true, "Available slots", bookedSlots);

                default:
                    return new ErrorResponse("Unknown master action");
            }
        } catch (Exception e) {
            return new ErrorResponse("Master service error: " + e.getMessage());
        }
    }

    private Response processService(ServiceCommand cmd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            switch (cmd.getAction()) {
                case GET_ALL:
                    List<Service> services = session.createQuery(
                            "FROM Service", Service.class).list();
                    return new ListResponse<>(true, "Services retrieved", services);

                case GET_BY_MASTER:
                    Query<Service> query = session.createQuery(
                            "SELECT s FROM Service s JOIN s.masters m WHERE m.id = :masterId",
                            Service.class);
                    query.setParameter("masterId", cmd.getMasterId());
                    return new ListResponse<>(true, "Master services", query.getResultList());

                default:
                    return new ErrorResponse("Unknown service action");
            }
        } catch (Exception e) {
            return new ErrorResponse("Service error: " + e.getMessage());
        }
    }
}