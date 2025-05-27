package org.sspd.myatdental.invoiceoptions.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import org.sspd.myatdental.treatmentoptions.model.TreatmentInvoiceRecordId;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice_treatment_records")
@Component
@Getter
@Setter
public class TreatmentInvoiceRecord {

    @EmbeddedId
    private TreatmentInvoiceRecordId id;

    @ManyToOne
    @MapsId("treatmentInvoice")
    @JoinColumn(name = "invoice_id", nullable = false)
    private TreatmentInvoice treatmentInvoice;

    @ManyToOne
    @MapsId("treatmentRecord")
    @JoinColumn(name = "record_id", nullable = false)
    private TreatmentRecord treatmentRecord;



}
