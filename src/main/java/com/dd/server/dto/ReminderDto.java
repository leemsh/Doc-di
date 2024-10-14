package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReminderDto {
    private String email;
    private String medicineName;
    private String dosage;
    private String recurrence;
    private String endDate;
    private String medicationTime;
    private String medicationTaken;
}
