package com.dd.server.dto;

import lombok.Data;

@Data
public class CheckCodeDto {
    private String email;
    private String code;
}
