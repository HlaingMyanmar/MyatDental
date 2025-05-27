package org.sspd.myatdental.invoiceoptions.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoice;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoiceRecord;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.paymentoptions.model.Payment;
import org.sspd.myatdental.paymentoptions.service.PaymentService;
import org.sspd.myatdental.treatmentoptions.impl.TreatmentInvoiceRecordimpl;
import org.sspd.myatdental.treatmentoptions.model.TreatmentInvoiceRecordId;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;
import org.sspd.myatdental.treatmentoptions.service.TreatmentRecordService;


import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class TreatmentInvoiceRecordService {


    private final SessionFactory sessionFactory;

    private final AppointmentService appointmentService;

    private final TreatmentInvoiceRecordimpl treatmentInvoiceRecordDao;

    private final TreatmentRecordService treatmentRecordService;

    private final TreatmentInvoiceService treatmentInvoiceService;

    private final PaymentService paymentService;


    public TreatmentInvoiceRecordService(SessionFactory sessionFactory, AppointmentService appointmentService, TreatmentInvoiceRecordimpl treatmentInvoiceRecordDao, TreatmentRecordService treatmentRecordService, TreatmentInvoiceService treatmentInvoiceService, PaymentService paymentService) {
        this.sessionFactory = sessionFactory;
        this.appointmentService = appointmentService;
        this.treatmentInvoiceRecordDao = treatmentInvoiceRecordDao;
        this.treatmentRecordService = treatmentRecordService;
        this.treatmentInvoiceService = treatmentInvoiceService;
        this.paymentService = paymentService;
    }

    public boolean addTreatmentRecordInvoicePayment(
            List<TreatmentRecord> treatmentRecords,
            TreatmentInvoice treatmentInvoice,
            Payment payment
    ) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Save invoice first so it has ID
            session.persist(treatmentInvoice);
            session.flush(); // Ensure invoiceId is generated

            int batchSize = 10;
            for (int i = 0; i < treatmentRecords.size(); i++) {
                TreatmentRecord record = treatmentRecords.get(i);
                record.setTreatmentInvoice(treatmentInvoice);
                session.persist(record);
                session.flush(); // ensure recordId is generated

                // Build ID and link record
                TreatmentInvoiceRecordId id = new TreatmentInvoiceRecordId(
                        treatmentInvoice.getInvoiceId(),
                        record.getRecordId()
                );
                TreatmentInvoiceRecord tir = new TreatmentInvoiceRecord();
                tir.setId(id);
                tir.setTreatmentInvoice(treatmentInvoice);
                tir.setTreatmentRecord(record);

                session.persist(tir);

                if (i % batchSize == 0 && i > 0) {
                    session.flush();
                    session.clear();
                }
            }

            // Link payment to invoice and persist
            payment.setTreatmentInvoice(treatmentInvoice);
            session.persist(payment);



            // Update appointment status directly
            Appointment appointment = treatmentInvoice.getAppointment();
            if (appointment != null) {
                appointment.setStatus("Completed");
                session.merge(appointment); // updates existing appointment
            }



            tx.commit();

            AlertBox.showInformationDialog("Invoice", "",
                    "Successfully saved " + treatmentRecords.size() + " treatment records, invoice, and payment.");
            return true;

        } catch (Exception e) {

            System.err.println("Error in addTreatmentRecordInvoicePayment: " + e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            return false;
        }
    }







}