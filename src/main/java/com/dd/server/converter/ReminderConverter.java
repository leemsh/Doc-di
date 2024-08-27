package com.dd.server.converter;

import com.dd.server.domain.Reminder;
import com.dd.server.dto.ReminderDto;

public class ReminderConverter {
    public static Reminder toEntity(ReminderDto reminderDto){
        Reminder reminder = new Reminder();
        reminder.setEmail(reminderDto.getEmail());
        reminder.setMedicineName(reminderDto.getMedicineName());
        reminder.setMedicineUnit(reminderDto.getMedicineUnit());
        reminder.setOneTimeAmount(reminderDto.getOneTimeAmount());
        reminder.setOneTimeCount(reminderDto.getOneTimeCount());
        reminder.setEatingDays(reminderDto.getEatingDays());
        reminder.setEatingStartDate(reminderDto.getEatingStartDate());
        return reminder;
    }
}
