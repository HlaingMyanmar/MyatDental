package org.sspd.myatdental.appointmentsoptions.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.ErrorHandler.AppointmentConflictException;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.util.List;

@Repository
public class PatientAppointmentService {

    private final SessionFactory sessionFactory;

    public PatientAppointmentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean addPatientAppointment(Patient patient, Appointment appointment) throws AppointmentConflictException {
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
        } catch (ConstraintViolationException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new AppointmentConflictException("This doctor already has an appointment at this date and time.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            // Log the error for debugging
            e.printStackTrace();
            return false;
        }
    }


  //  @Transactional(readOnly = true)
    public List<Appointment[]> getDentistAppointments(int dentistId, String status) {


        try (Session session = sessionFactory.getCurrentSession()) {
            String hql = "SELECT a.appointment_date, a.appointment_time " +
                    "FROM Appointment a " +
                    "WHERE a.dentist.dentist_id = :dentistId AND a.status = :status " +
                    "ORDER BY a.appointment_date, a.appointment_time";
            Query<Appointment[]> query = session.createQuery(hql, Appointment[].class);
            query.setParameter("dentistId", dentistId);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {

            return List.of(); // Return empty list on error
        }
    }
}

