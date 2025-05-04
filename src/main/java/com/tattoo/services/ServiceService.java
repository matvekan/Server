package com.tattoo.services;

import com.tattoo.config.HibernateUtil;
import com.tattoo.models.Service;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ServiceService {
    public static List<Service> getAllServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Service> query = session.createQuery("FROM Service WHERE isActive = true", Service.class);
            return query.list();
        }
    }

    public static List<Service> getServicesByMaster(Long masterId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Service s JOIN s.masters m WHERE m.id = :masterId AND s.isActive = true";
            Query<Service> query = session.createQuery(hql, Service.class);
            query.setParameter("masterId", masterId);
            return query.list();
        }
    }
}