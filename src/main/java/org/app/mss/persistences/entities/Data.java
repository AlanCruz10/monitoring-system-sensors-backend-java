package org.app.mss.persistences.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Double valor;

    @Column
    private String sensor;

    @Column
    private Integer day;

    @Column
    private Integer mount;

    @Column
    private Integer year;

    @Column
    private Integer hour;

    @Column
    private Integer minute;

    @Column
    private Integer second;

}