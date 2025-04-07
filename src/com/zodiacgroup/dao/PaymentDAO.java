package com.zodiacgroup.dao;

import com.zodiacgroup.model.Payment;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class PaymentDAO {
	public List<Payment> getAllPayments() {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        return session.createQuery("FROM Payment", Payment.class).getResultList();
	    } catch (Exception e) {
	        System.err.println("Error retrieving payments: " + e.getMessage());
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
    public Payment getPaymentById(int invoiceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Payment.class, invoiceId);
        }
    }
    
    public boolean saveOrUpdatePayment(Payment payment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    
    
    public void deletePayment(Payment payment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(payment);
            transaction.commit();
        }
    }
    
    public List<Payment> searchPayments(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Payment p where p.customerName like :searchTerm or " +
                         "p.invoiceId like :searchTerm or p.status like :searchTerm";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.list();
        }
    }
    
   
}