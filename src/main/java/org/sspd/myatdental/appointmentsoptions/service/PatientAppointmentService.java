package org.sspd.myatdental.appointmentsoptions.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.ErrorHandler.AppointmentConflictException;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.deletion.model.DeletionLog;
import org.sspd.myatdental.deletion.service.DeletionService;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.useroptions.Users;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PatientAppointmentService {

    private final SessionFactory sessionFactory;

    private final DeletionService deletionService;

    public PatientAppointmentService(SessionFactory sessionFactory, DeletionService deletionService) {
        this.sessionFactory = sessionFactory;
        this.deletionService = deletionService;
    }

    public boolean addPatientAppointment(Patient patient, Appointment appointment) throws AppointmentConflictException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            // Save the patient first if not already persisted
            if (patient.getPatient_id() == 0) { // Assuming Patient has an ID field
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

    public boolean deletePatientAppointment(Patient patient, Appointment appointment, Users user,String reason) throws AppointmentConflictException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Set session variables for the trigger
            session.createNativeQuery("SET @current_user_name = :userName")
                    .setParameter("userName", user.getDisplayName())
                    .executeUpdate();

            session.createNativeQuery("SET @delete_reason = :reason")
                    .setParameter("reason", reason != null && !reason.trim().isEmpty() ? reason +"\nAppointment deleted by user : "+user.getDisplayName() : "Appointment deleted by user: " + user.getDisplayName())
                    .executeUpdate();

            // Fetch the existing appointment
            Appointment existingAppointment = session.get(Appointment.class, appointment.getAppointment_id());
            if (existingAppointment == null) {
                throw new AppointmentConflictException("Appointment does not exist.");
            }
            if (existingAppointment.getPatient().getPatient_id() != patient.getPatient_id()) {
                throw new AppointmentConflictException("Appointment does not belong to the specified patient.");
            }

            // Fetch the patient to ensure it exists
            Patient existingPatient = session.get(Patient.class, patient.getPatient_id());
            if (existingPatient == null) {
                throw new AppointmentConflictException("Patient does not exist.");
            }

            // Delete the appointment (triggers DeletionLog insertion)
            try {
                session.remove(existingAppointment);
            } catch (Exception e) {
                throw new AppointmentConflictException("Failed to delete appointment: " + e.getMessage());
            }

            // Delete the patient
            try {
                session.remove(existingPatient);
            } catch (Exception e) {
                throw new AppointmentConflictException("Failed to delete patient: " + e.getMessage());
            }

            tx.commit();
            System.out.println("Transaction committed successfully.");
            return true;
        } catch (ConstraintViolationException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.out.println("Transaction rolled back due to constraint violation: " + e.getMessage());
            }
            throw new AppointmentConflictException("Cannot delete patient or appointment due to a constraint violation: " + e.getMessage());
        } catch (AppointmentConflictException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.out.println("Transaction rolled back due to: " + e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.out.println("Transaction rolled back due to unexpected error: " + e.getMessage());
            }
            throw new AppointmentConflictException("Error deleting patient or appointment: " + e.getMessage());
        }
    }

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

