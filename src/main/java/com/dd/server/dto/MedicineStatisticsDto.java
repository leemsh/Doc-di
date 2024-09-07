package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicineStatisticsDto {
    private String name;
    private int rateTotal;
    private int rateAmount;
}
