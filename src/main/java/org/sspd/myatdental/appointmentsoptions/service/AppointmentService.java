package org.sspd.myatdental.appointmentsoptions.service;


import org.springframework.stereotype.Service;
import org.sspd.myatdental.appointmentsoptions.impl.Appointmentimpl;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;

import java.util.List;

@Service
public class AppointmentService {

    private final Appointmentimpl appointmentimpl;

    public AppointmentService(Appointmentimpl appointmentimpl) {
        this.appointmentimpl = appointmentimpl;
    }

    public List<Appointment> getAppointments() {

        return appointmentimpl.findAll();

    }

    public List<AppointmentView> getAppointmentViews() {
        return appointmentimpl.getAppointments();
    }

    public boolean addAppointment(Appointment appointment) {
        return appointmentimpl.save(appointment);
    }

    public boolean updateAppointment(Appointment appointment) {
        return appointmentimpl.update(appointment);
    }

    public boolean deleteAppointment(Appointment appointment) {
        return appointmentimpl.delete(appointment);
    }

    public boolean deleteById(int id) {
        return appointmentimpl.deleteById(id);
    }


}
