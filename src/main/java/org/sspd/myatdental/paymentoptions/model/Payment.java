package org.sspd.myatdental.paymentoptions.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import org.sspd.myatdental.invoiceoptions.model.TreatmentInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;


    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private TreatmentInvoice treatmentInvoice;


    public enum PaymentMethod {
        Cash, Bank_Transfer, Mobile_Money
    }
}
