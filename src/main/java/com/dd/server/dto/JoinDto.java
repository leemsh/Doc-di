package com.dd.server.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class JoinDto {
    private String email;
    private String password;
    private String name;
    private String image;
}
