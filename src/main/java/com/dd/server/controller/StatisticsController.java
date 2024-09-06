package com.dd.server.controller;

import com.dd.server.domain.Statistics;
import com.dd.server.dto.StatisticsDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticsController {
    private  final StatisticsService statisticsService;

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Statistics>>> getStatistics(@RequestParam(required = true) String medicineName){
        SuccessResponse<List<Statistics>> response = statisticsService.getStatistics(medicineName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping(value="/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createStatistics(@RequestBody StatisticsDto statisticsDto){
        SuccessResponse<String> response = statisticsService.createStatistics(statisticsDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value="/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> editStatistics(@RequestBody Statistics statistics){
        SuccessResponse<String> response = statisticsService.updateStatistics(statistics);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @DeleteMapping(value="/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> deleteStatistics(@RequestParam(required = true) int id){
        SuccessResponse<String> response = statisticsService.deleteStatistics(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
