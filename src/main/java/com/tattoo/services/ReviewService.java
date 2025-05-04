package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Appointment;
import com.tattoo.models.Review;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReviewService {
    public static boolean addReview(Long appointmentId, Integer rating, String comment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Review review = new Review();
            review.setAppointment(session.load(Appointment.class, appointmentId));
            review.setRating(rating);
            review.setComment(comment);

            session.save(review);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Review> getReviewsByMaster(Long masterId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT r FROM Review r WHERE r.appointment.master.id = :masterId";
            return session.createQuery(hql, Review.class)
                    .setParameter("masterId", masterId)
                    .list();
        }
    }
}