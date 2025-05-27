package org.sspd.myatdental.invoiceoptions.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.sspd.myatdental.invoiceoptions.impl.TreatmentInvoiceRecordimpl;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoice;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoiceRecord;
import org.sspd.myatdental.paymentoptions.model.Payment;
import org.sspd.myatdental.paymentoptions.service.PaymentService;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;
import org.sspd.myatdental.treatmentoptions.service.TreatmentRecordService;


import java.util.List;

@Service
public class TreatmentInvoiceRecordService {


    private final SessionFactory sessionFactory;

    private final TreatmentInvoiceRecordimpl treatmentInvoiceRecordDao;

    private final TreatmentRecordService treatmentRecordService;

    private final TreatmentInvoiceService treatmentInvoiceService;

    private final PaymentService paymentService;


    public TreatmentInvoiceRecordService(SessionFactory sessionFactory, TreatmentInvoiceRecordimpl treatmentInvoiceRecordDao, TreatmentRecordService treatmentRecordService, TreatmentInvoiceService treatmentInvoiceService, PaymentService paymentService) {
        this.sessionFactory = sessionFactory;
        this.treatmentInvoiceRecordDao = treatmentInvoiceRecordDao;
        this.treatmentRecordService = treatmentRecordService;
        this.treatmentInvoiceService = treatmentInvoiceService;
        this.paymentService = paymentService;
    }



    private boolean addTreatmentRecordInvoicePayment(
            List<TreatmentRecord> treatmentRecords,
            TreatmentInvoice treatmentInvoice,
            Payment payment,
            TreatmentInvoiceRecord treatmentInvoiceRecord) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Validate inputs
            if ((treatmentRecords == null || treatmentRecords.isEmpty()) && treatmentInvoiceRecord == null) {
                return false;
            }
            if (treatmentInvoice == null || payment == null) {
                return false;
            }


            double paymentAmount = payment.getAmount();
            if (paymentAmount == 0.0 || paymentAmount<-0.0) {
                return false;
            }

            assert treatmentRecords != null;
            for(TreatmentRecord record : treatmentRecords) {

                treatmentRecordService.save(record);

            }

            treatmentInvoiceService.createInvoice(treatmentInvoice);
            paymentService.createPayment(payment);







            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error in addTreatmentRecordInvoicePayment: " + e.getMessage());
            return false;
        }
    }






}