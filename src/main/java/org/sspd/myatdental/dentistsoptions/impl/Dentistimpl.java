package org.sspd.myatdental.dentistsoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.dto.DataAccessObject;

import java.util.List;

@Repository
public class Dentistimpl implements DataAccessObject<Dentist> {

    private final SessionFactory sessionFactory;

    public Dentistimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Dentist> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Dentist ", Dentist.class).list();
        }
    }

    @Override
    public boolean save(Dentist dentist) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx= session.beginTransaction();
            session.persist(dentist);
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
    public boolean delete(Dentist dentist) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(dentist);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Dentist dentist) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(dentist);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Dentist findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Dentist dentist= session.get(Dentist.class, id);
            if (dentist != null) {
                session.remove(dentist);
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
    public boolean updateById(Dentist dentist) {
        return false;
    }
}
