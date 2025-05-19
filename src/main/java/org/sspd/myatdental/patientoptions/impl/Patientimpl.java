package org.sspd.myatdental.patientoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.util.List;

@Repository
public class Patientimpl implements DataAccessObject<Patient> {

    private final SessionFactory sessionFactory;

    public Patientimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Patient> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Patient ", Patient.class).list();
        }
    }

    @Override
    public boolean save(Patient patient) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx= session.beginTransaction();
            session.persist(patient);
            tx.commit();
            return true;
        } catch (Exception e) {
            assert tx != null;
            tx.rollback();

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(patient);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(patient);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Patient findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Patient patient= session.get(Patient.class, id);
            if (patient != null) {
                session.remove(patient);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateById(Patient patient) {
        return false;
    }
}
