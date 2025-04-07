package com.zodiacgroup.dao;

import com.zodiacgroup.model.Appointment;
import com.zodiacgroup.model.Service;
import com.zodiacgroup.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Date;

public class ServiceDAO {
    
	public boolean saveService(Service service) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_insert_service_sr")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_CustomerName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_VehicleName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ServiceDetail", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Status", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ServiceDate", java.util.Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Cost", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_MechanicName", String.class, ParameterMode.IN)
                .setParameter("p_CustomerID", service.getCustomerId())
                .setParameter("p_CustomerName", service.getCustomerName())
                .setParameter("p_VehicleName", service.getVehicleName())
                .setParameter("p_ServiceDetail", service.getServiceDetail())
                .setParameter("p_Status", service.getStatus())
                .setParameter("p_ServiceDate", service.getDate())
                .setParameter("p_Cost", service.getCost())
                .setParameter("p_MechanicName", service.getMechanicName());
            
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
    public List<String> getServiceTypes() {
        return Arrays.asList(
            "Oil Change",
            "Tire Rotation",
            "Brake Service",
            "Engine Diagnostic",
            "Transmission Service",
            "Battery Replacement",
            "Air Conditioning Service",
            "Wheel Alignment",
            "Exhaust System Repair",
            "General Inspection"
        );
    }
    
    public List<Service> getAllServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Service", Service.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
 // In ServiceDAO.java
    public List<Service> getServicesByCustomerId(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_get_services_by_customerid_sr")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ResultSet", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_CustomerID", customerId);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_ResultSet")) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("ServiceID"));
                    service.setCustomerId(customerId);
                    service.setCustomerName(rs.getString("CustomerName"));
                    service.setVehicleName(rs.getString("VehicleName"));
                    service.setServiceDetail(rs.getString("ServiceDetail"));
                    service.setStatus(rs.getString("Status"));
                    service.setDate(rs.getDate("ServiceDate"));
                    service.setCost(rs.getDouble("Cost"));
                    service.setMechanicName(rs.getString("MechanicName")); // Add this line
                    services.add(service);
                }
                return services;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public Service getServiceById(int serviceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_view_service_sr")
                .registerStoredProcedureParameter("p_ServiceID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ResultSet", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_ServiceID", serviceId);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_ResultSet")) {
                if (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("ServiceID"));
                    service.setCustomerId(rs.getInt("CustomerID"));
                    service.setCustomerName(rs.getString("CustomerName"));
                    service.setVehicleName(rs.getString("VehicleName"));
                    service.setServiceDetail(rs.getString("ServiceDetail"));
                    service.setStatus(rs.getString("Status"));
                    service.setDate(rs.getDate("ServiceDate"));
                    service.setCost(rs.getDouble("Cost"));
                    service.setMechanicName(rs.getString("MechanicName"));
                    return service;
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Service getServiceByType(String serviceType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Service> query = session.createQuery(
                "FROM Service WHERE serviceType = :serviceType", Service.class);
            query.setParameter("serviceType", serviceType);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // In ServiceDAO.java, update the updateService method
    public boolean updateService(Service service) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Convert java.util.Date to java.sql.Date if needed
            java.sql.Date sqlDate = new java.sql.Date(service.getDate().getTime());
            
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_update_service_sr")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(6, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(7, java.sql.Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter(8, Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter(9, String.class, ParameterMode.IN)
                .setParameter(1, service.getId())
                .setParameter(2, service.getCustomerId())
                .setParameter(3, service.getCustomerName())
                .setParameter(4, service.getVehicleName())
                .setParameter(5, service.getServiceDetail())
                .setParameter(6, service.getStatus())
                .setParameter(7, sqlDate)
                .setParameter(8, service.getCost())
                .setParameter(9, service.getMechanicName());
                
            query.execute();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // Add this method to ServiceDAO.java to get all mechanic names
    public List<String> getAllMechanicNames() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<String> query = session.createQuery(
                "SELECT CONCAT(m.firstName, ' ', m.lastName) FROM Mechanic m", String.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public boolean deleteService(int serviceId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // First delete any dependent payments
            Query<?> deletePayments = session.createNativeQuery(
                "DELETE FROM PAYMENTS WHERE SERVICEID = :serviceId");
            deletePayments.setParameter("serviceId", serviceId);
            deletePayments.executeUpdate();
            
            // Then delete the service using the correct procedure name
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_delete_service_sr")
                .registerStoredProcedureParameter("p_ServiceID", Integer.class, ParameterMode.IN)
                .setParameter("p_ServiceID", serviceId);
            
            query.execute();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Service> getServicesByVehicleId(int vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_get_services_by_vehicleid_sr")
                .registerStoredProcedureParameter("p_VehicleID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ResultSet", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_VehicleID", vehicleId);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_ResultSet")) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("ServiceID"));
                    service.setCustomerId(rs.getInt("CustomerID"));
                    service.setCustomerName(rs.getString("CustomerName"));
                    service.setVehicleName(rs.getString("VehicleName"));
                    service.setServiceDetail(rs.getString("ServiceDetail"));
                    service.setStatus(rs.getString("Status"));
                    service.setDate(rs.getDate("ServiceDate"));
                    service.setCost(rs.getDouble("Cost"));
                    service.setMechanicName(rs.getString("MechanicName"));
                    services.add(service);
                }
                return services;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
 // Add these methods to ServiceDAO.java

    public List<Service> searchServices(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_search_services_sr")
                .registerStoredProcedureParameter("p_SearchTerm", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ResultSet", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_SearchTerm", searchTerm); // Remove the % signs here
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_ResultSet")) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("ServiceID"));
                    service.setCustomerId(rs.getInt("CustomerID"));
                    service.setCustomerName(rs.getString("CustomerName"));
                    service.setVehicleName(rs.getString("VehicleName"));
                    service.setServiceDetail(rs.getString("ServiceDetail"));
                    service.setStatus(rs.getString("Status"));
                    service.setDate(rs.getDate("ServiceDate"));
                    service.setCost(rs.getDouble("Cost"));
                    service.setMechanicName(rs.getString("MechanicName"));
                    services.add(service);
                }
                return services;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Service> searchServicesByCustomerId(int customerId, String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("pp_search_services_by_customer_sr")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_SearchTerm", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_ResultSet", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_CustomerID", customerId)
                .setParameter("p_SearchTerm", searchTerm); // Remove the % signs here
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_ResultSet")) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("ServiceID"));
                    service.setCustomerId(customerId);
                    service.setCustomerName(rs.getString("CustomerName"));
                    service.setVehicleName(rs.getString("VehicleName"));
                    service.setServiceDetail(rs.getString("ServiceDetail"));
                    service.setStatus(rs.getString("Status"));
                    service.setDate(rs.getDate("ServiceDate"));
                    service.setCost(rs.getDouble("Cost"));
                    service.setMechanicName(rs.getString("MechanicName"));
                    services.add(service);
                }
                return services;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public Appointment getAppointmentById(int appointmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Appointment.class, appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
}