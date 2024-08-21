package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
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
    private String role;
    private String image;
}
