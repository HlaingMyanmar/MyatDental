package org.sspd.myatdental.paymentoptions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.paymentoptions.model.Payment;

import java.util.List;

@Service
public class PaymentService {

    private final DataAccessObject<Payment> paymentDao;

    @Autowired
    public PaymentService(DataAccessObject<Payment> paymentDao) {
        this.paymentDao = paymentDao;
    }

    /**
     * Retrieves all payments.
     * @return List of all Payment entities.
     */
    public List<Payment> getAllPayments() {
        return paymentDao.findAll();
    }

    /**
     * Retrieves a payment by its ID.
     * @param id The ID of the payment.
     * @return The Payment if found, or null if not found.
     */
    public Payment getPaymentById(int id) {
        return paymentDao.findById(id);
    }

    /**
     * Creates a new payment.
     * @param payment The Payment to save.
     * @return true if saved successfully, false otherwise.
     * @throws IllegalArgumentException if the payment is invalid.
     */
    public boolean createPayment(Payment payment) {
        if (payment == null || !isValidPayment(payment)) {
            throw new IllegalArgumentException("Invalid payment data");
        }
        return paymentDao.save(payment);
    }

    /**
     * Updates an existing payment.
     * @param payment The Payment with updated data.
     * @return true if updated successfully, false otherwise.
     * @throws IllegalArgumentException if the payment is invalid or lacks an ID.
     */
    public boolean updatePayment(Payment payment) {
        if (payment == null || payment.getPaymentId() == null || !isValidPayment(payment)) {
            throw new IllegalArgumentException("Invalid payment data or missing ID");
        }
        return paymentDao.update(payment);
    }

    /**
     * Deletes a payment by its ID.
     * @param id The ID of the payment to delete.
     * @return true if deleted successfully, false otherwise.
     */
    public boolean deletePaymentById(int id) {
        return paymentDao.deleteById(id);
    }

    /**
     * Validates a Payment before saving or updating.
     * @param payment The payment to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidPayment(Payment payment) {
        return payment.getInvoiceId() != null &&
                payment.getAmount() != null &&
                payment.getPaymentDate() != null &&
                payment.getAmount().signum() >= 0; // Ensure amount is non-negative
    }
}