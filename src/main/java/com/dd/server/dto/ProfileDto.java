package com.dd.server.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {
    private String email;
    private String name;
    private String image;
}
