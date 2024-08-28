package com.dd.server.service;
import com.dd.server.converter.ReminderConverter;
import com.dd.server.domain.Reminder;
import com.dd.server.dto.ReminderDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse<List<Reminder>> getReminder(String email){
        List<Reminder> data;
        try{
            data = reminderRepository.findByEmail(email);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("interner server error", 500);
        }
        if(data.isEmpty()){
            return new SuccessResponse("empty", 204);
        }
        return new SuccessResponse<>(data, 200);
    }

    public SuccessResponse<String> createReminder(ReminderDto reminderDto){
        Reminder reminder = ReminderConverter.toEntity(reminderDto);
        try{
            reminderRepository.save(reminder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse<>("DB save fail", 500);
        }
        return new SuccessResponse<>("DB save success", 201);
    }

    public SuccessResponse<String> updateReminder(Reminder reminder){
        Reminder data = reminderRepository.findById(reminder.getId());
        data.setMedicineName(reminder.getMedicineName());
        data.setMedicineUnit(reminder.getMedicineUnit());
        data.setOneTimeAmount(reminder.getOneTimeAmount());
        data.setOneTimeCount(reminder.getOneTimeCount());
        data.setEatingDays(reminder.getEatingDays());
        data.setEatingStartDate(reminder.getEatingStartDate());
        try {
            reminderRepository.save(data);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse<>("DB save fail", 500);
        }
        return new SuccessResponse<>("DB save success", 200);
    }

    public SuccessResponse<String> deleteReminder(int id){
        try {
            reminderRepository.deleteById(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse<>("DB delete fail", 500);
        }
        return new SuccessResponse<>("DB delete success", 200);
    }

    public SuccessResponse<String> deleteReminderByEmail(String email){
        try {
            reminderRepository.deleteByEmail(email);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse<>("DB delete fail", 500);
        }
        return new SuccessResponse<>("DB delete success", 200);
    }
}
