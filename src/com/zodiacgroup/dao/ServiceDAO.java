package com.zodiacgroup.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zodiacgroup.model.Service;
import com.zodiacgroup.util.HibernateUtil;

public class ServiceDAO {
    public void insertService(Service service) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(service); // Save service to the database
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback on failure
            }
            e.printStackTrace();
        }
    }
}
