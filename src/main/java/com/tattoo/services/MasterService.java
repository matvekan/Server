package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Master;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MasterService {
    public static List<Master> getAllMasters() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Master> query = session.createQuery("FROM Master", Master.class);
            return query.list();
        }
    }

    public static Master getMasterById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Master.class, id);
        }
    }

    public static List<Master> getAvailableMasters(LocalDateTime dateTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m FROM Master m WHERE m.id NOT IN " +
                    "(SELECT a.master.id FROM Appointment a WHERE a.dateTime = :dateTime)";
            return session.createQuery(hql, Master.class)
                    .setParameter("dateTime", dateTime)
                    .list();
        }
    }

    public static List<LocalDateTime> getAvailableTimeSlots(Long masterId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 1. Получаем рабочее время мастера (предположим 10:00-18:00)
            LocalTime workStart = LocalTime.of(10, 0);
            LocalTime workEnd = LocalTime.of(18, 0);

            // 2. Получаем все записи мастера на эту дату
            String hql = "SELECT a.dateTime FROM Appointment a " +
                    "WHERE a.master.id = :masterId " +
                    "AND CAST(a.dateTime AS date) = :date " +
                    "AND a.status != 'CANCELLED'";

            List<LocalDateTime> bookedSlots = session.createQuery(hql, LocalDateTime.class)
                    .setParameter("masterId", masterId)
                    .setParameter("date", date)
                    .list();

            // 3. Получаем услуги мастера для определения длительности
            Master master = session.get(Master.class, masterId);
            if (master == null) {
                return Collections.emptyList();
            }

            // 4. Генерируем доступные слоты (каждые 30 минут)
            List<LocalDateTime> availableSlots = new ArrayList<>();
            LocalDateTime currentSlot = LocalDateTime.of(date, workStart);
            LocalDateTime endTime = LocalDateTime.of(date, workEnd);

            while (currentSlot.isBefore(endTime)) {
                boolean isAvailable = true;

                // Проверяем не пересекается ли слот с существующими записями
                for (LocalDateTime booked : bookedSlots) {
                    if (currentSlot.equals(booked)) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    availableSlots.add(currentSlot);
                }

                currentSlot = currentSlot.plusMinutes(30); // Стандартный слот 30 минут
            }

            return availableSlots;
        } catch (Exception e) {
            System.err.println("Error getting available slots: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}