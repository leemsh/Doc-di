package com.dd.server.controller;


import com.dd.server.domain.Reminder;
import com.dd.server.dto.ReminderDto;
import com.dd.server.dto.StatisticsDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reminder")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Reminder>>> getRemiinder(@RequestParam(required = true) String email){
        SuccessResponse<java.util.List<Reminder>> response = reminderService.getReminder(email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping(value="/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createReminder(@RequestBody ReminderDto reminderDto){
        SuccessResponse<String> response = reminderService.createReminder(reminderDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value="/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> putReminder(@RequestBody Reminder reminder){
        SuccessResponse<String> response = reminderService.updateReminder(reminder);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @DeleteMapping(value="/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> deleteReminder(@RequestParam(required = true) int id){
        SuccessResponse<String> response = reminderService.deleteReminder(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
