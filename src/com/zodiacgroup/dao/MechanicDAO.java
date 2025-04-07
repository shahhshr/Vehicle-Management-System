package com.zodiacgroup.dao;

import com.zodiacgroup.model.Mechanic;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;

public class MechanicDAO {

    public void addMechanic(Mechanic mechanic) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.save(mechanic);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public List<Mechanic> getAllMechanics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Mechanic", Mechanic.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch mechanics", e);
        }
    }

    public Mechanic getMechanicById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Mechanic.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch mechanic with ID: " + id, e);
        }
    }

    public void updateMechanic(Mechanic mechanic) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.update(mechanic);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public void deleteMechanic(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            Mechanic mechanic = session.get(Mechanic.class, id);
            if (mechanic != null) {
                session.delete(mechanic);
            }
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    public List<Mechanic> searchMechanics(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Mechanic m WHERE " +
                         "LOWER(m.firstName) LIKE LOWER(:searchTerm) OR " +
                         "LOWER(m.lastName) LIKE LOWER(:searchTerm) OR " +
                         "LOWER(m.phoneNumber) LIKE LOWER(:searchTerm) OR " +
                         "LOWER(m.specialization) LIKE LOWER(:searchTerm) OR " +
                         "CAST(m.mechanicId AS string) LIKE :searchTerm";
            
            Query<Mechanic> query = session.createQuery(hql, Mechanic.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to search mechanics", e);
        }
    }
}


