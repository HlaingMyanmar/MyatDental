package org.sspd.myatdental.paymentoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.paymentoptions.model.Payment;

import java.util.List;

@Repository
public class PaymentImpl implements DataAccessObject<Payment> {

    private final SessionFactory sessionFactory;

    @Autowired
    public PaymentImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Payment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Payment> query = session.createQuery("FROM Payment", Payment.class);
            return query.getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public boolean save(Payment payment) {
        if (payment == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(payment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Payment payment) {
        if (payment == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(payment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        if (payment == null || payment.getPaymentId() == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(payment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Payment findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Payment.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Payment payment = session.get(Payment.class, id);
            if (payment == null) {
                session.getTransaction().rollback();
                return false;
            }
            session.remove(payment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateById(Payment payment) {
        if (payment == null || payment.getPaymentId() == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Payment existingPayment = session.get(Payment.class, payment.getPaymentId());
            if (existingPayment == null) {
                session.getTransaction().rollback();
                return false;
            }
            session.merge(payment);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}