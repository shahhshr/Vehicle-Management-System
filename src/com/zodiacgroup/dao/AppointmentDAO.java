package com.zodiacgroup.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.util.HibernateUtil;

public class AppointmentDAO {
    public void insertAppointment(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(appointment);  // Save appointment to the database
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback on failure
            }
            e.printStackTrace();
        }
    }
}
