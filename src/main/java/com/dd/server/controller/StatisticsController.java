package com.dd.server.controller;

import com.dd.server.domain.Statistics;
import com.dd.server.dto.StatisticsDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.StatisticsService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<SuccessResponse<List<Statistics>>> getStatistics(@RequestParam(required = true) String email){
        SuccessResponse<List<Statistics>> response = statisticsService.getStatistics(email);
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
}
