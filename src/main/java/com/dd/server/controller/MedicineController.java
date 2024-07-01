package com.dd.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    /*
    * 알약 검색 컨트롤러
    * path : /medicine/find
    * return : 약에 대한 정보
     */

    @GetMapping(value = "/find")
    public ResponseEntity<SuccessResponse> getMedicine(
            @RequestParam("color1") String color1,
            @RequestParam("color2") String color2,
            @RequestParam("shape") String shape,
            @RequestParam("frontTxt") String txt1,
            @RequestParam("backTxt") String txt2){
        SuccessResponse response = this.medicineService.getMedicine(color1, color2, shape, txt1, txt2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
