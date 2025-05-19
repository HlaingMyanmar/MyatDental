package org.sspd.myatdental.treatmentoptions.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "treatments")
@Component
@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int treatment_id;
    @NotBlank(message = "Required Treatment Name")
    @Column(name = "name",length = 100)
    private String name;

    @Column(name = "description")
    private String description;
    @NotNull(message = "Required Standard Price")
    @Column(name = "standard_price")
    private double standard_price;


    public Treatment(String name, String description, double standard_price) {
        this.name = name;
        this.description = description;
        this.standard_price = standard_price;
    }
}
