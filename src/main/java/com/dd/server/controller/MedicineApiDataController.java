package com.dd.server.controller;

import com.dd.server.dto.MedicineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
public class MedicineApiDataController {

    private final RestTemplate restTemplate;

    @Autowired
    public MedicineApiDataController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/medicine")
    public MedicineResponse getMedicineEntity(@RequestParam String chart) {
        String serviceKey = "%2FqfclHAZhBhEgnRIT3tgBVKrwqZc6EheBLisxgGFp%2FsTsd1TcivMatomLb1mn1ANjulMpmck74G3BiYhd6pIyg%3D%3D";
        String url = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=" + serviceKey + "&chart=" + chart + "&type=json";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // JSON 응답 요청

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<MedicineResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MedicineResponse.class
        );

        return response.getBody();
    }
}