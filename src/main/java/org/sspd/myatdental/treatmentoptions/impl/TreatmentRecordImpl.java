package org.sspd.myatdental.treatmentoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

import java.util.List;

@Repository
public class TreatmentRecordImpl implements DataAccessObject<TreatmentRecord> {

    private final SessionFactory sessionFactory;

    public TreatmentRecordImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TreatmentRecord> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<TreatmentRecord> query = session.createQuery("FROM TreatmentRecord", TreatmentRecord.class);
            return query.getResultList();
        } catch (Exception e) {
            // Consider logging the exception using a logging framework like SLF4J
            throw new RuntimeException("Failed to retrieve all treatment records", e);
        }
    }

    @Override
    public boolean save(TreatmentRecord treatmentRecord) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(treatmentRecord);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            return false;
        }
    }

    @Override
    public boolean delete(TreatmentRecord treatmentRecord) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.merge(treatmentRecord)); // Merge to ensure entity is managed
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            return false;
        }
    }

    @Override
    public boolean update(TreatmentRecord treatmentRecord) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(treatmentRecord);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            return false;
        }
    }

    @Override
    public TreatmentRecord findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(TreatmentRecord.class, id);
        } catch (Exception e) {
            // Log the exception
            return null;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TreatmentRecord record = session.get(TreatmentRecord.class, id);
            if (record != null) {
                session.remove(record);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            // Log the exception
            return false;
        }
    }

    @Override
    public boolean updateById(TreatmentRecord treatmentRecord) {
        return update(treatmentRecord); // Reuse update method
    }
}