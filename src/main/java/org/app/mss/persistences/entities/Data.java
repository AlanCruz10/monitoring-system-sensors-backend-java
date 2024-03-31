package org.app.mss.persistences.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "data")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column
    private String type;

    @Column
    private Double value;

    @Column
    private LocalDate date;

    @Column
    private LocalTime time;

    @Column
    private String sensor;

}