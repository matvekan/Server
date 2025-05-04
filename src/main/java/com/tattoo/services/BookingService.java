package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Appointment;
import com.tattoo.models.Master;
import com.tattoo.models.Service;
import com.tattoo.models.User;
import com.tattoo.network.responses.BookingResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class BookingService {
    public static BookingResponse bookAppointment(Long userId, Long masterId, Long serviceId,
                                                  LocalDateTime dateTime, String notes) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Проверка доступности времени
            boolean isAvailable = checkAvailability(session, masterId, dateTime);
            if (!isAvailable) {
                return new BookingResponse(false, "This time slot is already booked");
            }

            // Создание записи
            Appointment appointment = new Appointment();
            appointment.setUser(session.load(User.class, userId));
            appointment.setMaster(session.load(Master.class, masterId));
            appointment.setService(session.load(Service.class, serviceId));
            appointment.setDateTime(dateTime);
            appointment.setNotes(notes);
            appointment.setStatus("BOOKED");

            session.save(appointment);
            tx.commit();

            return new BookingResponse(true, "Appointment booked successfully");
        } catch (Exception e) {
            return new BookingResponse(false, "Error booking appointment: " + e.getMessage());
        }
    }

    private static boolean checkAvailability(Session session, Long masterId, LocalDateTime dateTime) {
        String hql = "SELECT COUNT(a) FROM Appointment a WHERE a.master.id = :masterId " +
                "AND a.dateTime = :dateTime AND a.status != 'CANCELLED'";

        Long count = session.createQuery(hql, Long.class)
                .setParameter("masterId", masterId)
                .setParameter("dateTime", dateTime)
                .uniqueResult();

        return count == 0;
    }
}