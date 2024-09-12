package com.dd.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="BOOKED")
public class Booked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //예약 아이디

    @Column(nullable = false)
    private String email; //사용자 이메일

    @Column(nullable = false)
    private String hospitalName; //병원이름

    @Column(nullable = false)
    private String doctorName; //의사이름

    @Column(nullable = false)
    private String subject; //병원이름

    @Column(nullable = false)
    private String bookTime; //예약시간
}
