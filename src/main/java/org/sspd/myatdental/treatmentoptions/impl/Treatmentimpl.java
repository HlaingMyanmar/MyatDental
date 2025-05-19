package org.sspd.myatdental.treatmentoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.treatmentoptions.model.Treatment;

import java.util.List;

@Repository
public class Treatmentimpl implements DataAccessObject<Treatment> {

    private final SessionFactory sessionFactory;

    public Treatmentimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Treatment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Treatment ", Treatment.class).list();
        }
    }

    @Override
    public boolean save(Treatment treatment) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx= session.beginTransaction();
            session.persist(treatment);
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
    public boolean delete(Treatment treatment) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(treatment);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Treatment treatment) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(treatment);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Treatment findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Treatment treatment= session.get(Treatment.class, id);
            if (treatment != null) {
                session.remove(treatment);
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
    public boolean updateById(Treatment treatment) {
        return false;
    }
}
