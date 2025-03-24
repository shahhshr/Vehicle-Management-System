package com.zodiacgroup.dao;

import com.zodiacgroup.model.Vehicle;
import com.zodiacgroup.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class VehicleDAO {

    // Add a new vehicle
    public void addVehicle(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Update an existing vehicle
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

    // Delete a vehicle
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

    // Get a vehicle by ID
    public Vehicle getVehicleById(int vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, vehicleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Vehicle> vehicles = session.createQuery("FROM Vehicle", Vehicle.class).list();
            System.out.println("Number of vehicles fetched: " + vehicles.size()); // Debugging
            return vehicles;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }
}