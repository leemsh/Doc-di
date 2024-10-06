package com.dd.server.dto;

import lombok.Data;

@Data
public class SearchHistoryDto {
    private String email;
    private String medicineName;
    private String itemSeq;
    private String searchTime;
}
