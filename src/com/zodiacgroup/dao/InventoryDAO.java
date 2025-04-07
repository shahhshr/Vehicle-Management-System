package com.zodiacgroup.dao;

import com.zodiacgroup.model.Inventory;
import com.zodiacgroup.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Collections;
import java.util.List;

public class InventoryDAO {

    public void addInventoryItem(Inventory item) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public List<Inventory> getAllInventoryItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Inventory", Inventory.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch inventory items", e);
        }
    }

    public void updateInventoryItem(Inventory item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(item);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteInventoryItem(int inventoryId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Inventory item = session.get(Inventory.class, inventoryId);
            if (item != null) {
                session.delete(item);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Inventory getInventoryItemById(int inventoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Inventory.class, inventoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Inventory> searchInventoryById(String idSearch) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE CAST(inventoryId AS string) LIKE :id";
            return session.createQuery(hql, Inventory.class)
                         .setParameter("id", "%" + idSearch + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Inventory> searchInventoryByName(String itemName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE lower(itemName) LIKE lower(:name)";
            return session.createQuery(hql, Inventory.class)
                         .setParameter("name", "%" + itemName + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Inventory> searchInventoryByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE lower(category) LIKE lower(:category)";
            return session.createQuery(hql, Inventory.class)
                         .setParameter("category", "%" + category + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Inventory> searchLowStockItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE stockQuantity < minimumStockThreshold";
            return session.createQuery(hql, Inventory.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Inventory> searchInventoryBySupplier(String supplier) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Inventory WHERE lower(supplier) LIKE lower(:supplier)";
            return session.createQuery(hql, Inventory.class)
                         .setParameter("supplier", "%" + supplier + "%")
                         .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}