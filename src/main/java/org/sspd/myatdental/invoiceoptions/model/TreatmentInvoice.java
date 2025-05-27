package org.sspd.myatdental.invoiceoptions.model;

import lombok.*;
import org.springframework.stereotype.Component;
import jakarta.persistence.*;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.paymentoptions.model.Payment;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name = "invoices")
public class TreatmentInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "total_amount", nullable = false, scale = 2)
    private Double totalAmount;

    @Column(name = "balance_due", nullable = false,  scale = 2)
    private Double balanceDue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status = InvoiceStatus.Unpaid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "treatmentInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Payment> paymentSet;

    @OneToMany(mappedBy = "treatmentInvoice", cascade = CascadeType.ALL)
    private  Set<TreatmentRecord> treatmentRecordSet ;


    @OneToMany(mappedBy = "treatmentInvoice", cascade = CascadeType.ALL)
    private Set<TreatmentInvoiceRecord> treatmentInvoiceRecord;



    public enum InvoiceStatus {
        Unpaid, Partially_Paid, Paid
    }

    public TreatmentInvoice(Integer discount, Double totalAmount, Double balanceDue, InvoiceStatus status, Patient patient, Appointment appointment) {
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.balanceDue = balanceDue;
        this.status = status;
        this.patient = patient;
        this.appointment = appointment;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}