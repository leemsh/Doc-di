package com.dd.server.dto;

import lombok.Data;

@Data
public class GeminiResponseDataDto {
    private String title;
    private String question;
    private String answer;
}
