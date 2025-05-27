package org.sspd.myatdental.treatmentoptions.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
public class TreatmentInvoiceRecordId implements Serializable {

    private Integer treatmentInvoice;
    private Integer treatmentRecord;
}