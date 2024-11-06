package com.dd.server.dto;

import com.dd.server.domain.Medicine;
import lombok.Data;

import java.util.List;

@Data
public class RasaDto {
    private String recipient_id;
    private String text;
    private List<RasaButtonDto> buttons;
    private RasaCustomDto custom;
    private List<Medicine> medicineList;
    private List<GeminiResponseDataDto> data;
}
