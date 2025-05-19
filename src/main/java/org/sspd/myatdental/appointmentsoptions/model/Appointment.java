package org.sspd.myatdental.appointmentsoptions.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointment_id;

    @Column(name = "appointment_date")
    @NotBlank(message = "Required Date")
    private Date appointment_date;

    @Column(name = "appointment_time")
    @NotBlank(message = "Required Time")
    private Time appointment_time;

    @Column(name = "status",length = 15)
    private String status;

    @NotBlank(message = "Required Purpose")
    @Column(name = "purpose")
    private String purpose;

    @Column(name = "notes")
    private String notes;

    @Column(name = "create_at")
    private Timestamp created_at;

    @ManyToOne()
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;

    @ManyToOne()
    @JoinColumn(name="dentist_id", nullable = false)
    private Dentist dentist;





}
