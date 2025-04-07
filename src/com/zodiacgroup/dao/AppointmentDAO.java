package com.zodiacgroup.dao;

import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.ParameterMode;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.StoredProcedureQuery;

public class AppointmentDAO {

	public boolean saveAppointment(Appointment appointment) {
	    Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        
	        StoredProcedureQuery query = session.createStoredProcedureQuery("Insert_Appointment")
	            .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("p_VehicleID", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("p_CustomerName", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("p_AppointmentDate", java.util.Date.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("p_ServiceType", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("p_Status", String.class, ParameterMode.IN)
	            .setParameter("p_CustomerID", appointment.getCustomerId())
	            .setParameter("p_VehicleID", appointment.getVehicleId())
	            .setParameter("p_CustomerName", appointment.getCustomerName())
	            .setParameter("p_AppointmentDate", appointment.getAppointmentDate())
	            .setParameter("p_ServiceType", appointment.getServiceType())
	            .setParameter("p_Status", appointment.getStatus());
	        
	        query.execute();
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

    public void updateAppointment(Appointment appointment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Update_Appointment")
                .registerStoredProcedureParameter("p_AppointmentID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_VehicleID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_CustomerName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_AppointmentDate", java.util.Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ServiceType", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Status", String.class, ParameterMode.IN)
                .setParameter("p_AppointmentID", appointment.getAppointmentId())
                .setParameter("p_CustomerID", appointment.getCustomerId())
                .setParameter("p_VehicleID", appointment.getVehicleId())
                .setParameter("p_CustomerName", appointment.getCustomerName())
                .setParameter("p_AppointmentDate", appointment.getAppointmentDate())
                .setParameter("p_ServiceType", appointment.getServiceType())
                .setParameter("p_Status", appointment.getStatus());
            
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update appointment via stored procedure", e);
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Delete_Appointment")
                .registerStoredProcedureParameter("p_AppointmentID", Integer.class, ParameterMode.IN)
                .setParameter("p_AppointmentID", appointmentId);
            
            query.execute();
            return true; // If no exception was thrown, assume deletion was successful
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete appointment via stored procedure", e);
        }
    }

    public Appointment getAppointmentById(int appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Get_Appointment")
                .registerStoredProcedureParameter("p_AppointmentID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_AppointmentID", appointmentId);

            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                if (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(appointmentId);
                    appointment.setCustomerId(rs.getInt("CustomerID"));
                    appointment.setVehicleId(rs.getInt("VehicleID"));
                    appointment.setCustomerName(rs.getString("CustomerName"));
                    appointment.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                    appointment.setServiceType(rs.getString("ServiceType"));
                    appointment.setStatus(rs.getString("Status"));
                    return appointment;
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get appointment via stored procedure", e);
        }
    }

    public static List<Appointment> getAllAppointments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Appointment";
            return session.createQuery(hql, Appointment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Appointment> getAppointmentsByCustomerId(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Get_Customer_Appointments")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_CustomerID", customerId);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                List<Appointment> appointments = new ArrayList<>();
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("AppointmentID"));
                    appointment.setCustomerId(customerId);
                    appointment.setVehicleId(rs.getInt("VehicleID"));
                    appointment.setCustomerName(rs.getString("CustomerName"));
                    appointment.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                    appointment.setServiceType(rs.getString("ServiceType"));
                    appointment.setStatus(rs.getString("Status"));
                    appointments.add(appointment);
                }
                return appointments;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get customer appointments via stored procedure", e);
        }
    }
    
    public List<Appointment> getAppointmentsByVehicleId(int vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Get_Vehicle_Appointments")
                .registerStoredProcedureParameter("p_VehicleID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_VehicleID", vehicleId);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                List<Appointment> appointments = new ArrayList<>();
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("AppointmentID"));
                    appointment.setCustomerId(rs.getInt("CustomerID"));
                    appointment.setVehicleId(vehicleId);
                    appointment.setCustomerName(rs.getString("CustomerName"));
                    appointment.setAppointmentDate(rs.getTimestamp("AppointmentDate"));
                    appointment.setServiceType(rs.getString("ServiceType"));
                    appointment.setStatus(rs.getString("Status"));
                    appointments.add(appointment);
                }
                return appointments;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get vehicle appointments via stored procedure", e);
        }
    }
    
 // Add these methods to your AppointmentDAO class
 // In AppointmentDAO.java

    public List<Appointment> searchAppointmentsByCustomerName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Appointment WHERE lower(customerName) LIKE lower(:name)";
            return session.createQuery(hql, Appointment.class)
                         .setParameter("name", "%" + name + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Appointment> searchAppointmentsByServiceType(String serviceType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Appointment WHERE lower(serviceType) LIKE lower(:serviceType)";
            return session.createQuery(hql, Appointment.class)
                         .setParameter("serviceType", "%" + serviceType + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Appointment> searchAppointmentsByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Appointment WHERE lower(status) LIKE lower(:status)";
            return session.createQuery(hql, Appointment.class)
                         .setParameter("status", "%" + status + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
 // In AppointmentDAO.java

    public List<Appointment> searchAppointments(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Search_Appointments")
                .registerStoredProcedureParameter("p_SearchTerm", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_SearchTerm", "%" + searchTerm + "%");

            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                List<Appointment> appointments = new ArrayList<>();
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("APPOINTMENTID"));
                    appointment.setCustomerId(rs.getInt("CUSTOMERID"));
                    appointment.setVehicleId(rs.getInt("VEHICLEID"));
                    appointment.setCustomerName(rs.getString("CUSTOMERNAME"));
                    appointment.setAppointmentDate(rs.getTimestamp("APPOINTMENTDATE"));
                    appointment.setServiceType(rs.getString("SERVICETYPE"));
                    appointment.setStatus(rs.getString("STATUS"));
                    appointments.add(appointment);
                }
                return appointments;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
}
    
