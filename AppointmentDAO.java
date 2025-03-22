package com.zodiacgroup.dao;

import com.zodiacgroup.model.Appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentDAO {

    // In-memory storage for appointments
    private final Map<String, Appointment> appointments = new HashMap<>();

    // Insert an appointment (Create)
    public void insertAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() == null) {
            System.err.println("Invalid appointment data.");
            return;
        }
        appointments.put(appointment.getAppointmentId(), appointment);
        System.out.println("Appointment inserted successfully: " + appointment);
    }

    // Update an appointment 
    public void updateAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() == null) {
            System.err.println("Invalid appointment data.");
            return;
        }
        if (appointments.containsKey(appointment.getAppointmentId())) {
            appointments.put(appointment.getAppointmentId(), appointment);
            System.out.println("Appointment updated successfully: " + appointment);
        } else {
            System.out.println("No appointment found with ID: " + appointment.getAppointmentId());
        }
    }

    // Retrieve an appointment by ID (Read)
    public Appointment getAppointmentById(String appointmentId) {
        if (appointmentId == null) {
            System.err.println("Invalid appointment ID.");
            return null;
        }
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            System.out.println("Appointment retrieved successfully: " + appointment);
        } else {
            System.out.println("No appointment found with ID: " + appointmentId);
        }
        return appointment;
    }

    // Delete an appointment by ID (Delete)
    public boolean deleteAppointment(int appointmentId) {
        // Convert the int appointmentId to a String since the Map uses String keys
        String appointmentIdStr = String.valueOf(appointmentId);

        if (appointments.containsKey(appointmentIdStr)) {
            appointments.remove(appointmentIdStr);
            System.out.println("Appointment deleted successfully with ID: " + appointmentId);
            return true; // Deletion successful
        } else {
            System.out.println("No appointment found with ID: " + appointmentId);
            return false; // Deletion failed (appointment not found)
        }
    }

    // Retrieve all appointments (Optional)
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>(appointments.values());
        System.out.println("Retrieved all appointments: " + appointmentList.size() + " found.");
        return appointmentList;
    }
}