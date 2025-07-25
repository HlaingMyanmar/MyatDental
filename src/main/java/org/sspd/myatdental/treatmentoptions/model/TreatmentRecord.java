package org.sspd.myatdental.treatmentoptions.model;

import jakarta.persistence.*;
import lombok.*;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoice;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoiceRecord;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "treatment_records")
public class TreatmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;


    @Column(name = "tooth_number", length = 20)
    private String toothNumber;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TreatmentRecord", nullable = false)
    private TreatmentInvoice treatmentInvoice;



    @OneToMany(mappedBy = "treatmentRecord", cascade = CascadeType.ALL)
    private Set<TreatmentInvoiceRecord> treatmentInvoiceRecords;


    public TreatmentRecord(Appointment appointment, Treatment treatment, String toothNumber, String notes, double price) {
        this.appointment = appointment;
        this.treatment = treatment;
        this.toothNumber = toothNumber;
        this.notes = notes;
        this.price = price;
    }
}