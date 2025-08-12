package com.pluralsight.ticket_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Passport {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    //we gonna have only column passport_id inside student table , the owner of relationship is student
    @OneToOne(fetch=FetchType.EAGER , mappedBy = "passport")
    private Student student;

}
