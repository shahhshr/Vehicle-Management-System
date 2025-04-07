package com.zodiacgroup.dao;

import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Inventory;
import com.zodiacgroup.model.Notification;
import com.zodiacgroup.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DashboardDAO {
    // For Appointments
	    public List<Appointment> getUpcomingAppointments(Date startDate, Date endDate) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            String hql = "FROM Appointment WHERE appointmentDate BETWEEN :startDate AND :endDate ORDER BY appointmentDate ASC";
	            return session.createQuery(hql, Appointment.class)
	                         .setParameter("startDate", startDate)
	                         .setParameter("endDate", endDate)
	                         .list();
	        } catch (Exception e) {
	            System.err.println("Error fetching appointments: " + e.getMessage());
	            return Collections.emptyList();
	        }
	    }
	    public List<Notification> getRecentNotifications(int limit) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            return session.createQuery(
	                "FROM Notification WHERE type = 'stock' ORDER BY timestamp DESC", 
	                Notification.class)
	                .setMaxResults(limit)
	                .list();
	        } catch (Exception e) {
	            System.err.println("Error fetching notifications: " + e.getMessage());
	            return Collections.emptyList();
	        }
	    }

    // For Quick Actions (statistics)
    public long getPendingAppointmentsCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM Appointment WHERE status = 'Pending'";
            return (long) session.createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch pending appointments count", e);
        }
    }

    public long getLowStockItemsCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM Inventory WHERE stockQuantity < minimumStockThreshold";
            return (long) session.createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch low stock items count", e);
        }
    }
    
    public List<Inventory> getLowStockItems(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE stockQuantity < minimumStockThreshold ORDER BY stockQuantity ASC";
            return session.createQuery(hql, Inventory.class)
                         .setMaxResults(limit)
                         .list();
        } catch (Exception e) {
            System.err.println("Error fetching low stock items: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    
}