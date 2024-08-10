package com.dd.server.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class JoinDto {

    private int id;
    private String email;
    private String password;
    private String name;
    private String sex;
    private String birthday;
    private short height;
    private short weight;
    private String bloodType;
    private String phoneNum;
}
