package org.sspd.myatdental.appointmentsoptions.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.patientoptions.model.Patient;

@Repository
public class PatientAppointmentService {

    private final SessionFactory sessionFactory;


    public PatientAppointmentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public boolean addPatientAppointment(Patient patient, Appointment appointment) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            // Save the patient first if not already persisted
            if (patient.getPatient_id() == null) { // Assuming Patient has an ID field
                session.persist(patient);
            }
            appointment.setPatient(patient);
            session.persist(appointment);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }


}
