package com.dd.server.dto;

import lombok.Data;
import java.util.List;

@Data
public class NaverApiResponseDto {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverSearchItemDto> items;
}