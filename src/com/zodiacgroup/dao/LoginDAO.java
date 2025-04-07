package com.zodiacgroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.zodiacgroup.model.Login;
import com.zodiacgroup.util.HibernateUtil;

import javafx.scene.control.Alert;

public class LoginDAO {
    public Login getLoginByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Login) session.createNativeQuery(
                "SELECT * FROM login WHERE LOWER(username) = LOWER(:username)", Login.class)
                .setParameter("username", username)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean validateLogin(String username, String password) {
        try {
            Login login = getLoginByUsername(username);
            return login != null && login.getPassword().equals(password); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void createInitialAdmin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Login existingAdmin = session.createQuery("FROM Login WHERE role = 'ADMIN'", Login.class)
                                      .setMaxResults(1)
                                      .uniqueResult();
            
            if (existingAdmin == null) {
                Transaction transaction = session.beginTransaction();
                Login admin = new Login();
                admin.setUsername("admin");
                admin.setPassword("admin123"); 
                admin.setRole("ADMIN");
                session.save(admin);
                transaction.commit();
            }
        }
    }
    
    public void createLogin(Login login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(login);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create login", e);
        }
    }
    
    public void updateLogin(String oldUsername, Login newLogin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            if (!oldUsername.equals(newLogin.getUsername())) {
                session.createQuery("DELETE FROM Login WHERE username = :username")
                      .setParameter("username", oldUsername)
                      .executeUpdate();
            }
            session.saveOrUpdate(newLogin);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update login", e);
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}