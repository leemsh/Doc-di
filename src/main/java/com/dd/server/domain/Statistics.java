package com.dd.server.domain;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "STATISTICS")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String medicineName;

    @Column
    private String statistic;

    @Column(nullable = false)
    private short rate;
}
