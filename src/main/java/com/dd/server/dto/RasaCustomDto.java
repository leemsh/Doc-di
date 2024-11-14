package com.dd.server.dto;

import lombok.Data;

@Data
public class RasaCustomDto {
    private String action;
    private FindByMedicineChartDto data;
    private String done;
}
