package org.sspd.myatdental.patientoptions.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;

import java.sql.Date;
import java.util.Set;

@Component
@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String patient_id;

    @Column(name = "name",length = 100)
    @NotBlank(message = "Please Fill Name")
    private String name;

    @Column(name = "phone",length = 30)
    @NotBlank(message = "Please Fill Phone")
    private String phone;

    @Column(name = "township",length = 30)
    @NotBlank(message = "Please Fill Township")
    private String township;

    @Column(name = "address",length = 200)
    private String address;


    @Column(name = "date_of_birth",length = 200)
    private Date date_of_birth;

    @Column(name = "age",length = 3)
    private int age;

    @Column(name = "gender",length = 15)
    @NotBlank(message = "Please Fill Gender")
    private String gender;

    @Column(name = "medical_history",length = 15)
    @NotBlank(message = "Please Fill Medical History")
    private String medical_history;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Appointment> appointmentSet;

    public Patient(String name, String phone, String township, String address, Date date_of_birth, int age, String gender, String medical_history) {
        this.name = name;
        this.phone = phone;
        this.township = township;
        this.address = address;
        this.date_of_birth = date_of_birth;
        this.age = age;
        this.gender = gender;
        this.medical_history = medical_history;
    }
}
