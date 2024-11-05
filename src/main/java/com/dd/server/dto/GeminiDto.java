package com.dd.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeminiDto {
    private String sender;
    private List<GeminiSenderDataDto> data;
}
