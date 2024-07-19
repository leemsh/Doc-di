package com.dd.server.controller;

import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.Find;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

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
            // 여기서 이 값들을 키밸류값으로 api 요청이 들어옴
            @RequestBody @Valid FindByMedicineChartDto findByMedicineChartDto){
        SuccessResponse response = this.medicineService.getMedicine(findByMedicineChartDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
