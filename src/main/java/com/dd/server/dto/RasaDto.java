package com.dd.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class RasaDto {
    private String recipient_id;
    private String text;
    private List<RasaButtonDto> buttons;
}
