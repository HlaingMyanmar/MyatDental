package org.sspd.myatdental.invoiceoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoiceRecord;

import java.util.List;

@Repository
public class TreatmentInvoiceRecordimpl implements DataAccessObject<TreatmentInvoiceRecord> {

    private final SessionFactory sessionFactory;

    public TreatmentInvoiceRecordimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TreatmentInvoiceRecord> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<TreatmentInvoiceRecord> query = session.createQuery(
                    "FROM TreatmentInvoiceRecord", TreatmentInvoiceRecord.class);
            return query.list();
        } catch (Exception e) {
            // Replace with proper logging (e.g., SLF4J)
            System.err.println("Error finding all TreatmentInvoiceRecords: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean save(TreatmentInvoiceRecord treatmentInvoiceRecord) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(treatmentInvoiceRecord);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error saving TreatmentInvoiceRecord: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(TreatmentInvoiceRecord treatmentInvoiceRecord) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(treatmentInvoiceRecord);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error deleting TreatmentInvoiceRecord: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TreatmentInvoiceRecord treatmentInvoiceRecord) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(treatmentInvoiceRecord);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error updating TreatmentInvoiceRecord: " + e.getMessage());
            return false;
        }
    }

    @Override
    public TreatmentInvoiceRecord findById(int id) {
        // Note: Since TreatmentInvoiceRecord has a composite key (invoice_id, record_id),
        // this method may not be applicable. Consider using a composite key object or separate method.
        throw new UnsupportedOperationException("findById not supported for composite key. Use findByInvoiceAndRecordId instead.");
    }

    public TreatmentInvoiceRecord findByInvoiceAndRecordId(int invoiceId, int recordId) {
        try (Session session = sessionFactory.openSession()) {
            Query<TreatmentInvoiceRecord> query = session.createQuery(
                    "FROM TreatmentInvoiceRecord WHERE TreatmentInvoice .invoiceId = :invoiceId AND treatmentRecord.recordId = :recordId",
                    TreatmentInvoiceRecord.class);
            query.setParameter("invoiceId", invoiceId);
            query.setParameter("recordId", recordId);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error finding TreatmentInvoiceRecord by IDs: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteById(int id) {
        // Note: Not applicable for composite key. Use deleteByInvoiceAndRecordId instead.
        throw new UnsupportedOperationException("deleteById not supported for composite key. Use deleteByInvoiceAndRecordId instead.");
    }

    public boolean deleteByInvoiceAndRecordId(int invoiceId, int recordId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Query<?> query = session.createQuery(
                    "DELETE FROM TreatmentInvoiceRecord WHERE TreatmentInvoice .invoiceId = :invoiceId AND treatmentRecord.recordId = :recordId");
            query.setParameter("invoiceId", invoiceId);
            query.setParameter("recordId", recordId);
            int result = query.executeUpdate();
            tx.commit();
            return result > 0;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error deleting TreatmentInvoiceRecord by IDs: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateById(TreatmentInvoiceRecord treatmentInvoiceRecord) {
        // Since update requires the entity, this can reuse the update method
        return update(treatmentInvoiceRecord);
    }
}