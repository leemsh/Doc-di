package com.dd.server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REMINDER")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //리마인더 아이디

    @Column(nullable = false)
    private String email; //사용자 이메일

    @Column(nullable = false)
    private String medicineName; //약이름

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String recurrence;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String medicationTime;

    @Column(nullable = false)
    private String medicationTaken;
}
