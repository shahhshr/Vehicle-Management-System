package com.zodiacgroup.dao;

import com.zodiacgroup.model.Customer;
import com.zodiacgroup.model.Vehicle;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.Query;

public class VehicleDAO {

	public void addVehicle(Vehicle vehicle) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction transaction = null;
	    try {
	        transaction = session.beginTransaction();
	        
	        Customer customer = session.get(Customer.class, vehicle.getCustomer().getCustomerId());
	        if (customer == null) {
	            throw new RuntimeException("Customer not found");
	        }
	        vehicle.setCustomer(customer);
	        
	        session.save(vehicle);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        throw new RuntimeException("Failed to add vehicle", e);
	    } finally {
	        session.close();
	    }
	}

	public List<Vehicle> getAllVehicles() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM com.zodiacgroup.model.Vehicle", Vehicle.class).list();
		} catch (Exception e) {
            throw new RuntimeException("Failed to fetch customers", e);
        }
    }

	public void updateVehicle(Vehicle vehicle) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(vehicle);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public void deleteVehicle(int vehicleId) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Vehicle vehicle = session.get(Vehicle.class, vehicleId);
			if (vehicle != null) {
				session.delete(vehicle);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public Vehicle getVehicleById(int vehicleId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        return session.createQuery(
	            "FROM Vehicle WHERE vehicleId = :id", Vehicle.class)
	            .setParameter("id", vehicleId)
	            .uniqueResult();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public Vehicle getVehicleWithCustomer(int vehicleId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        return session.createQuery(
	            "FROM Vehicle v LEFT JOIN FETCH v.customer WHERE v.vehicleId = :id", 
	            Vehicle.class)
	            .setParameter("id", vehicleId)
	            .uniqueResult();
	    }
	}
	
	public List<Vehicle> getVehiclesByCustomerId(int customerId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM Vehicle WHERE customerId = :customerId";
	        javax.persistence.Query query = session.createQuery(hql);
	        query.setParameter("customerId", customerId);
	        return (List<Vehicle>) query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	public List<Vehicle> searchVehicles(String searchText) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM Vehicle v WHERE " +
	                     "v.make LIKE :search OR " +
	                     "v.model LIKE :search OR " + 
	                     "v.vin LIKE :search OR " +
	                     "v.customer.firstName LIKE :search OR " +
	                     "v.customer.lastName LIKE :search";
	        
	        return session.createQuery(hql, Vehicle.class)
	                     .setParameter("search", "%" + searchText + "%")
	                     .list();
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to search vehicles", e);
	    }
	}
	
	public Vehicle getVehicleByCustomerId(int customerId) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        String hql = "FROM Vehicle WHERE customer.customerId = :customerId";
	        return session.createQuery(hql, Vehicle.class)
	                    .setParameter("customerId", customerId)
	                    .uniqueResult();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	

}