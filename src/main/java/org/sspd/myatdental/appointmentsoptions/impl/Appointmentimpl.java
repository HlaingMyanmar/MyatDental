package org.sspd.myatdental.appointmentsoptions.impl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.dto.DataAccessObject;

import java.util.List;

@Repository
public class Appointmentimpl implements DataAccessObject<Appointment> {

    private final SessionFactory sessionFactory;


    public Appointmentimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Appointment ", Appointment.class).list();
        }
    }

    public List<AppointmentView> getAppointments() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT new org.sspd.myatdental.appointmentsoptions.model.AppointmentView(" +
                    "a.appointment_id AS appointmentCode, " +
                    "a.appointment_date AS date, " +
                    "a.appointment_time AS time, " +
                    "d.name AS doctorName, " +
                    "p.name AS patientName, " +
                    "p.phone AS patientPhone, " +
                    "p.date_of_birth AS dateOfBirth, " +
                    "p.gender AS gender, " +
                    "a.status AS status, " +
                    "a.purpose AS purpose, " +
                    "p.township AS township) " +
                    "FROM Appointment a " +
                    "JOIN a.patient p " +
                    "JOIN a.dentist d";

            return session.createQuery(hql, AppointmentView.class).list();
        }
    }




    @Override
    public boolean save(Appointment appointment) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx= session.beginTransaction();
            session.persist(appointment);
            tx.commit();
            return true;
        } catch (Exception e) {
            assert tx != null;
            tx.rollback();

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(appointment);
            tx.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(appointment);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Appointment findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Appointment appointment= session.get(Appointment.class, id);
            if (appointment != null) {
                session.remove(appointment);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateById(Appointment appointment) {
        return false;
    }
}
