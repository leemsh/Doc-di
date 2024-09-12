package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookedDto {
    private String email;
    private String hospitalName;
    private String doctorName;
    private String subject;
    private String bookTime;
}
