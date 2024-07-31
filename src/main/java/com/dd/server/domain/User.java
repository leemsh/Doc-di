package com.dd.server.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
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

    @Column(nullable = false)
    private String phoneNum;

    public void updateRefreshToken (String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken = null;
    }
}
