package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicineStatisticsDto {
    private String itemSeq;
    private int rateTotal;
    private int rateAmount;
}
