package org.sspd.myatdental.invoiceoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoice;

import java.util.List;

@Repository
public class TreatmentInvoiceImpl implements DataAccessObject<TreatmentInvoice> {

    private final SessionFactory sessionFactory;

    public TreatmentInvoiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TreatmentInvoice> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<TreatmentInvoice> query = session.createQuery("FROM TreatmentInvoice", TreatmentInvoice.class);
            return query.getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public boolean save(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(treatmentInvoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(treatmentInvoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null || treatmentInvoice.getInvoiceId() == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(treatmentInvoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TreatmentInvoice findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(TreatmentInvoice.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TreatmentInvoice treatmentInvoice = session.get(TreatmentInvoice.class, id);
            if (treatmentInvoice == null) {
                session.getTransaction().rollback();
                return false;
            }
            session.remove(treatmentInvoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateById(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null || treatmentInvoice.getInvoiceId() == null) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TreatmentInvoice existingInvoice = session.get(TreatmentInvoice.class, treatmentInvoice.getInvoiceId());
            if (existingInvoice == null) {
                session.getTransaction().rollback();
                return false;
            }
            session.merge(treatmentInvoice);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}