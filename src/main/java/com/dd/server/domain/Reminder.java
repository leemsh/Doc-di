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
    private String medicineUnit; //약 단위

    @Column(nullable = false)
    private short oneTimeAmount; //1회투여량

    @Column(nullable = false)
    private short oneTimeCount; //1회투여횟수

    @Column(nullable = false)
    private short eatingDays; //복용기간

    @Column(nullable = false)
    private String eatingStartDate; //복용시작일자
}
