package com.zodiacgroup.dao;

import com.zodiacgroup.model.SaleRepresentative;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Collections;
import java.util.List;

public class SaleDAO {

    public void addSaleRepresentative(SaleRepresentative saleRep) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(saleRep);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to add sales representative", e);
        }
    }

    public void updateSaleRepresentative(SaleRepresentative saleRep) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(saleRep);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update sales representative", e);
        }
    }

    public void deleteSaleRepresentative(int employeeId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SaleRepresentative saleRep = session.get(SaleRepresentative.class, employeeId);
            if (saleRep != null) {
                session.delete(saleRep);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete sales representative", e);
        }
    }

    public SaleRepresentative getSaleRepresentativeById(int employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SaleRepresentative.class, employeeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get sales representative", e);
        }
    }

    public List<SaleRepresentative> getAllSaleRepresentatives() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM SaleRepresentative", SaleRepresentative.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all sales representatives", e);
        }
    }

    public List<SaleRepresentative> searchSaleRepresentatives(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SaleRepresentative WHERE CAST(employeeId AS string) LIKE :search OR " +
                         "LOWER(employeeName) LIKE LOWER(:search) OR " +
                         "LOWER(position) LIKE LOWER(:search) OR " +
                         "LOWER(email) LIKE LOWER(:search) OR " +
                         "phoneNumber LIKE :search OR " +
                         "LOWER(username) LIKE LOWER(:search)";
            return session.createQuery(hql, SaleRepresentative.class)
                         .setParameter("search", "%" + searchTerm + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}