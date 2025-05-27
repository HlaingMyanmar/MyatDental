package org.sspd.myatdental.invoiceoptions.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice_treatment_records")
@Component
@Getter@Setter
public class TreatmentInvoiceRecord {

    @Id
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private TreatmentInvoice treatmentInvoice;


    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private TreatmentRecord treatmentRecord;







}
