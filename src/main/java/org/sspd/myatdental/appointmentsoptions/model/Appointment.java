package org.sspd.myatdental.appointmentsoptions.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoice;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

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
    private Date appointment_date;

    @Column(name = "appointment_time")
    private Time appointment_time;

    @Column(name = "status",length = 15)
    private String status;

    @NotBlank(message = "Required Purpose")
    @Column(name = "purpose")
    private String purpose;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private Timestamp created_at;

    @ManyToOne()
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;

    @ManyToOne()
    @JoinColumn(name="dentist_id", nullable = false)
    private Dentist dentist;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<TreatmentInvoice> treatmentInvoiceSet;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<TreatmentRecord> treatmentRecordSet;




    public Appointment(Date appointment_date, Time appointment_time, String status, String purpose, String notes, Patient patient, Dentist dentist) {
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
        this.purpose = purpose;
        this.notes = notes;
        this.patient = patient;
        this.dentist = dentist;
    }

    public Appointment(Date appointment_date, Time appointment_time, String status, String purpose, String notes, Dentist dentist) {
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
        this.purpose = purpose;
        this.notes = notes;
        this.dentist = dentist;
    }

    public Appointment(int appointment_id, Date appointment_date, Time appointment_time, String status, String purpose, String notes, Patient patient, Dentist dentist) {
        this.appointment_id = appointment_id;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
        this.purpose = purpose;
        this.notes = notes;
        this.patient = patient;
        this.dentist = dentist;
    }


}
