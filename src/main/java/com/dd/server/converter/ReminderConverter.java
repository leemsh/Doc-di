package com.dd.server.converter;

import com.dd.server.domain.Reminder;
import com.dd.server.dto.ReminderDto;

public class ReminderConverter {
    public static Reminder toEntity(ReminderDto reminderDto){
        Reminder reminder = new Reminder();
        reminder.setEmail(reminderDto.getEmail());
        reminder.setMedicineName(reminderDto.getMedicineName());
        reminder.setDosage(reminderDto.getDosage());
        reminder.setRecurrence(reminderDto.getRecurrence());
        reminder.setEndDate(reminderDto.getEndDate());
        reminder.setMedicationTime(reminderDto.getMedicationTime());
        reminder.setMedicationTaken(reminderDto.getMedicationTaken());
        return reminder;
    }
}
