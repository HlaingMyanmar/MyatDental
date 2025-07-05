package org.sspd.myatdental.treatmentoptions.model;


import jakarta.persistence.*;
import lombok.*;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoice;

import java.util.Set;

@Getter@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "treatment_categories")
@Entity
public class TreatmentCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int category_id;

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "treatmentCategory", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Treatment> treatments;




}
