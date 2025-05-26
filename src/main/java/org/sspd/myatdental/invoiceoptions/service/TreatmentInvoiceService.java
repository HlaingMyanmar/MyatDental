package org.sspd.myatdental.invoiceoptions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoice;

import java.util.List;

@Service
public class TreatmentInvoiceService {

    private final DataAccessObject<TreatmentInvoice> invoiceDao;

    @Autowired
    public TreatmentInvoiceService(DataAccessObject<TreatmentInvoice> invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    /**
     * Retrieves all treatment invoices.
     * @return List of all TreatmentInvoice entities.
     */
    public List<TreatmentInvoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    /**
     * Retrieves a treatment invoice by its ID.
     * @param id The ID of the invoice.
     * @return The TreatmentInvoice if found, or null if not found.
     */
    public TreatmentInvoice getInvoiceById(int id) {
        return invoiceDao.findById(id);
    }

    /**
     * Creates a new treatment invoice.
     * @param treatmentInvoice The TreatmentInvoice to save.
     * @return true if saved successfully, false otherwise.
     * @throws IllegalArgumentException if the invoice is invalid.
     */
    public boolean createInvoice(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null || !isValidInvoice(treatmentInvoice)) {
            throw new IllegalArgumentException("Invalid invoice data");
        }
        return invoiceDao.save(treatmentInvoice);
    }

    /**
     * Updates an existing treatment invoice.
     * @param treatmentInvoice The TreatmentInvoice with updated data.
     * @return true if updated successfully, false otherwise.
     * @throws IllegalArgumentException if the invoice is invalid or lacks an ID.
     */
    public boolean updateInvoice(TreatmentInvoice treatmentInvoice) {
        if (treatmentInvoice == null || treatmentInvoice.getInvoiceId() == null || !isValidInvoice(treatmentInvoice)) {
            throw new IllegalArgumentException("Invalid invoice data or missing ID");
        }
        return invoiceDao.update(treatmentInvoice);
    }

    /**
     * Deletes a treatment invoice by its ID.
     * @param id The ID of the invoice to delete.
     * @return true if deleted successfully, false otherwise.
     */
    public boolean deleteInvoiceById(int id) {
        return invoiceDao.deleteById(id);
    }

    /**
     * Validates a TreatmentInvoice before saving or updating.
     * @param treatmentInvoice The invoice to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidInvoice(TreatmentInvoice treatmentInvoice) {
        return treatmentInvoice.getPatient().getPatient_id() != 0 &&
                treatmentInvoice.getTotalAmount() != null &&
                treatmentInvoice.getBalanceDue() != null &&
                treatmentInvoice.getStatus() != null;
    }
}