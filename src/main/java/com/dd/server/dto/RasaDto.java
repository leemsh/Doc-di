package com.dd.server.dto;

import lombok.Data;

@Data
public class RasaDto {
    private int status;
    private String message;
    private String action;
    private String data;
}
