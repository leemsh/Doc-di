package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReminderDto {
    private String email;
    private String medicineName;
    private String medicineUnit;
    private short oneTimeAmount;
    private short oneTimeCount;
    private short eatingDays;
    private String eatingStartDate;
}
