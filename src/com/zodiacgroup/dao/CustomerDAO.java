package com.zodiacgroup.dao;

import com.zodiacgroup.model.Customer;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.HibernateException;
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

public class CustomerDAO {

	public void addCustomer(Customer customer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Insert_Customer")
                .registerStoredProcedureParameter("p_FirstName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_LastName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Phone", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Address", String.class, ParameterMode.IN)
                .setParameter("p_FirstName", customer.getFirstName())
                .setParameter("p_LastName", customer.getLastName())
                .setParameter("p_Email", customer.getEmail())
                .setParameter("p_Phone", customer.getPhoneNumber())
                .setParameter("p_Address", customer.getAddress());
            
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add customer via stored procedure", e);
        }
    }

    public void updateCustomer(Customer customer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Update_Customer")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_FirstName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_LastName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Phone", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Address", String.class, ParameterMode.IN)
                .setParameter("p_CustomerID", customer.getCustomerId())
                .setParameter("p_FirstName", customer.getFirstName())
                .setParameter("p_LastName", customer.getLastName())
                .setParameter("p_Email", customer.getEmail())
                .setParameter("p_Phone", customer.getPhoneNumber())
                .setParameter("p_Address", customer.getAddress());
            
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update customer via stored procedure", e);
        }
    }

    public void deleteCustomer(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Delete_Customer")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .setParameter("p_CustomerID", customerId);
            
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete customer via stored procedure", e);
        }
    }

    public Customer getCustomerById(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Get_Customer")
                .registerStoredProcedureParameter("p_CustomerID", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR)
                .setParameter("p_CustomerID", customerId);

            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerID"));
                    customer.setFirstName(rs.getString("FirstName"));
                    customer.setLastName(rs.getString("LastName"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setPhoneNumber(rs.getString("Phone"));
                    customer.setAddress(rs.getString("Address"));
                    return customer;
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get customer via stored procedure", e);
        }
    }

    public Customer getCustomerWithVehicles(int customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Customer c LEFT JOIN FETCH c.vehicles WHERE c.customerId = :id";
            return session.createQuery(hql, Customer.class)
                         .setParameter("id", customerId)
                         .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Customer> getAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StoredProcedureQuery query = session.createStoredProcedureQuery("Get_All_Customers")
                .registerStoredProcedureParameter("p_Result", void.class, ParameterMode.REF_CURSOR);
            
            query.execute();
            
            try (ResultSet rs = (ResultSet) query.getOutputParameterValue("p_Result")) {
                List<Customer> customers = new ArrayList<>();
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerID"));
                    customer.setFirstName(rs.getString("FirstName"));
                    customer.setLastName(rs.getString("LastName"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setPhoneNumber(rs.getString("Phone"));
                    customer.setAddress(rs.getString("Address"));
                    customers.add(customer);
                }
                return customers;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all customers", e);
        }
    }

    public List<Customer> searchCustomersById(String idSearch) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Customer WHERE CAST(customerId AS string) LIKE :id";
            return session.createQuery(hql, Customer.class)
                         .setParameter("id", "%" + idSearch + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

 public List<Customer> searchCustomersByName(String firstName, String lastName) {
     try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         String hql = "FROM Customer WHERE lower(firstName) LIKE lower(:firstName) AND lower(lastName) LIKE lower(:lastName)";
         return session.createQuery(hql, Customer.class)
                      .setParameter("firstName", "%" + firstName + "%")
                      .setParameter("lastName", "%" + lastName + "%")
                      .list();
     } catch (Exception e) {
         e.printStackTrace();
         return Collections.emptyList();
     }
 }

 public List<Customer> searchCustomersByFirstName(String firstName) {
     try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         String hql = "FROM Customer WHERE lower(firstName) LIKE lower(:firstName)";
         return session.createQuery(hql, Customer.class)
                      .setParameter("firstName", "%" + firstName + "%")
                      .list();
     } catch (Exception e) {
         e.printStackTrace();
         return Collections.emptyList();
     }
 }

 public List<Customer> searchCustomersByLastName(String lastName) {
     try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         String hql = "FROM Customer WHERE lower(lastName) LIKE lower(:lastName)";
         return session.createQuery(hql, Customer.class)
                      .setParameter("lastName", "%" + lastName + "%")
                      .list();
     } catch (Exception e) {
         e.printStackTrace();
         return Collections.emptyList();
     }
 }
 

   
}