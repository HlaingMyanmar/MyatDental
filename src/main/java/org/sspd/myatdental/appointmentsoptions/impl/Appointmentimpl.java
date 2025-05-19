package org.sspd.myatdental.appointmentsoptions.impl;


import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
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
        return List.of();
    }

    @Override
    public boolean save(Appointment appointment) {
        return false;
    }

    @Override
    public boolean delete(Appointment appointment) {
        return false;
    }

    @Override
    public boolean update(Appointment appointment) {
        return false;
    }

    @Override
    public Appointment findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public boolean updateById(Appointment appointment) {
        return false;
    }
}
