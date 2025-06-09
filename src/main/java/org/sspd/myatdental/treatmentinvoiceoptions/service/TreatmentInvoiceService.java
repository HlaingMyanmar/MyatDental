package org.sspd.myatdental.treatmentinvoiceoptions.service;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoice;

import java.util.List;

@Service
public class TreatmentInvoiceService {

    private final DataAccessObject<TreatmentInvoice> invoiceDao;
    private final SessionFactory sessionFactory;

    public TreatmentInvoiceService(DataAccessObject<TreatmentInvoice> invoiceDao, SessionFactory sessionFactory) {
        this.invoiceDao = invoiceDao;
        this.sessionFactory = sessionFactory;
    }

    // Save a new invoice
    public void saveInvoice(TreatmentInvoice invoice) {
        invoiceDao.save(invoice);
    }

    // Get all invoices
    public List<TreatmentInvoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    // Get invoice by ID
    public TreatmentInvoice getInvoiceById(int id) {
        return invoiceDao.findById(id);
    }

    // Update an invoice
    public void updateInvoice(TreatmentInvoice invoice) {
        invoiceDao.update(invoice);
    }

    // Delete invoice by ID
    public void deleteInvoice(int id) {
        TreatmentInvoice invoice = getInvoiceById(id);
        if (invoice != null) {
            invoiceDao.delete(invoice);
        }
    }

    // Get invoices by patient ID using Hibernate query
    public List<TreatmentInvoice> getInvoicesByPatientId(Long patientId) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM TreatmentInvoice WHERE patient.id = :patientId", TreatmentInvoice.class)
                    .setParameter("patientId", patientId)
                    .getResultList();
        }
    }




}
