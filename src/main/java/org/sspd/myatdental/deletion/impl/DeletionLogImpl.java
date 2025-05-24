package org.sspd.myatdental.deletion.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.deletion.model.DeletionLog;
import org.sspd.myatdental.dto.DataAccessObject;

import java.util.List;

@Repository
public class DeletionLogImpl implements DataAccessObject<DeletionLog> {

    private final SessionFactory sessionFactory;

    public DeletionLogImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<DeletionLog> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<DeletionLog> query = session.createQuery("FROM DeletionLog", DeletionLog.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all deletion logs", e);
        }
    }

    @Override
    public boolean save(DeletionLog deletionLog) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(deletionLog);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error saving deletion log", e);
        }
    }

    @Override
    public boolean delete(DeletionLog deletionLog) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(deletionLog);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting deletion log", e);
        }
    }

    @Override
    public boolean update(DeletionLog deletionLog) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(deletionLog);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating deletion log", e);
        }
    }

    @Override
    public DeletionLog findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(DeletionLog.class, (long) id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding deletion log by ID: " + id, e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DeletionLog deletionLog = session.get(DeletionLog.class, (long) id);
            if (deletionLog != null) {
                session.remove(deletionLog);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting deletion log by ID: " + id, e);
        }
    }

    @Override
    public boolean updateById(DeletionLog deletionLog) {
        if (deletionLog.getId() == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DeletionLog existingLog = session.get(DeletionLog.class, deletionLog.getId());
            if (existingLog != null) {
                session.merge(deletionLog);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error updating deletion log by ID: " + deletionLog.getId(), e);
        }
    }
}