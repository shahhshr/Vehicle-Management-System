package com.zodiacgroup.dao;

import com.zodiacgroup.model.Customer;
import com.zodiacgroup.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CustomerDAO {

    // Add a new customer
	public void addCustomer(Customer customer) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        System.out.println("Session opened: " + session.isOpen()); // Debugging
	        transaction = session.beginTransaction();
	        System.out.println("Transaction started: " + transaction.isActive()); // Debugging

	        session.save(customer);

	        transaction.commit();
	        System.out.println("Transaction committed: " + transaction.isActive()); // Debugging
	    } catch (Exception e) {
	        if (transaction != null) {
	            System.out.println("Transaction rollback: " + transaction.isActive()); // Debugging
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    }
	}

    // Update an existing customer
    public void updateCustomer(Customer customer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Delete a customer
    public void deleteCustomer(int customerId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, customerId);
            if (customer != null) {
                session.delete(customer);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Get a customer by ID
    public Customer getCustomerById(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Customer.class, customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}