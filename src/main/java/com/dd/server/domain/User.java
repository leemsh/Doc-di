package com.dd.server.domain;


import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private short height;

    @Column(nullable = false)
    private short weight;

    @Column(nullable = false, name = "blood_type")
    private String bloodType;

    @Column(length = 1000)
    private String refreshToken;

    @Column(nullable = false, unique = true, name = "phone_num")
    private String phoneNum;

    @Column(nullable = false)
    private String role;

    public void updateRefreshToken (String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken = null;
    }
}
