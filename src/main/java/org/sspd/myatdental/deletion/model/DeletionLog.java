package org.sspd.myatdental.deletion.model;

import lombok.*;
import org.springframework.stereotype.Component;
import jakarta.persistence.*;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "appointment_deletion_log")
public class DeletionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "dentist_id", nullable = false)
    private Integer dentistId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;


    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @Column(name = "reason")
    private String reason;



}