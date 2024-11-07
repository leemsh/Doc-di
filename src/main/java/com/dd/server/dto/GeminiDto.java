package com.dd.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeminiDto {
    private String sender;
    private String query;
    private List<GeminiSenderDataDto> data;
}
