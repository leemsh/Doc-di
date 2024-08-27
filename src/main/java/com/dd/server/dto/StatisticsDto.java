package com.dd.server.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticsDto {
    private String email;
    private String name;
    private String medicineName;
    private String statistic;
    private String date;
    private short rate;
}
