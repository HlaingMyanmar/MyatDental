package org.sspd.myatdental.dentistsoptions.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "dentists")
@Component
@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Dentist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int dentist_id;

    @NotBlank(message = "Required Name!!!")
    @Column(name = "name" ,length = 100 , nullable = false)
    private String name;

    @NotBlank(message = "Required Specialization")
    @NotNull
    @Column(name = "specialization",length = 100 , nullable = false)
    private String specialization;

    @NotBlank(message = "Required Phone")
    @NotNull
    @Column(name = "phone",length = 15 , nullable = false)
    private String phone;

    @NotBlank(message = "Required Email")
    @Email
    @Column(name = "email",length = 100 , nullable = false)
    private String email;


    public Dentist(String name, String specialization, String phone, String email) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }
}
