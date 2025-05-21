package org.sspd.myatdental.appointmentsoptions.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component

public class AppointmentView {


    private int appointment_id;
    private Date appointment_date;
    private Time appointment_time;
    private String doctor_name;
    private String patient_name;
    private String patient_phone;
    private Date date_of_birth;
    private String gender;
    private String status;
    private String purpose;
    private String township;



}
