package com.dd.server.domain;


import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "USERS")
public class Users {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true, name = "email")
    private String email;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String sex;

    @Column(nullable = false)
    private int birthday;

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

    public void updateRefreshToken (String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken = null;
    }
}
