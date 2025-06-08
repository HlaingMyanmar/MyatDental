package org.sspd.myatdental.treatmentoptions.model;


import jakarta.persistence.*;
import lombok.*;

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




}
