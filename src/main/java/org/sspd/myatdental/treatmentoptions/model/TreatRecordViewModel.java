package org.sspd.myatdental.treatmentoptions.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TreatRecordViewModel {

    private String toothNumber;
    private String notes;
    private double price;
    private String treatmentName;
}
