package com.dd.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CHECKCODE")
public class CheckCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //체크코드 아이디

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;
}
