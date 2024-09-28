package com.dd.server.controller;

import com.dd.server.domain.Booked;
import com.dd.server.domain.Reminder;
import com.dd.server.dto.BookedDto;
import com.dd.server.dto.ReminderDto;
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

    @GetMapping(value = "/medicine/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Reminder>>> getReminder(@RequestParam(required = true) String email){
        SuccessResponse<List<Reminder>> response = reminderService.getReminder(email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping(value="/medicine/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> createReminder(@RequestBody ReminderDto reminderDto){
        SuccessResponse<String> response = reminderService.createReminder(reminderDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value="/medicine/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> putReminder(@RequestBody Reminder reminder){
        SuccessResponse<String> response = reminderService.updateReminder(reminder);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @DeleteMapping(value="/medicine/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> deleteReminder(@RequestParam(required = true) int id){
        SuccessResponse<String> response = reminderService.deleteReminder(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }



    @GetMapping(value = "/booked/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Booked>>> getBookedReminder(@RequestParam(required = true) String email){
        SuccessResponse<List<Booked>> response = reminderService.getBookedReminder(email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping(value="/booked/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> createBookedReminder(@RequestBody BookedDto bookedDto){
        SuccessResponse<String> response = reminderService.createBookedReminder(bookedDto);
        HttpHeaders headers = new HttpHeaders();https://nedrug.mhttp://localhost:8080/reminder/booked/createfds.go.kr/pbp/cmn/itemImageDownload/1OKRXo9l4DN
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value="/booked/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> putBookedReminder(@RequestBody Booked booked){
        SuccessResponse<String> response = reminderService.updateBookedReminder(booked);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @DeleteMapping(value="/booked/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> deleteBookedReminder(@RequestParam(required = true) int id){
        SuccessResponse<String> response = reminderService.deleteBookedReminder(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
