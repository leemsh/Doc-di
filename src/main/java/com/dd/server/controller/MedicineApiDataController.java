package com.dd.server.controller;

import com.dd.server.dto.MedicineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MedicineApiDataController {

    @GetMapping("/medicine")
    public MedicineResponse getMedicineEntity(
            @RequestParam String chart) {
        RestTemplate restTemplate = new RestTemplate();

        String serviceKey = "%2FqfclHAZhBhEgnRIT3tgBVKrwqZc6EheBLisxgGFp%2FsTsd1TcivMatomLb1mn1ANjulMpmck74G3BiYhd6pIyg%3D%3D";
        String url = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=" + serviceKey + "&chart=" + chart;

        ResponseEntity<MedicineResponse> response = restTemplate.getForEntity(url, MedicineResponse.class);

        return response.getBody();
    }
}
