package com.dd.server.controller;

import com.dd.server.domain.Medicine;
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
import java.util.List;

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

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicine(
            @RequestParam(required = false) String color1,
            @RequestParam(required = false) String color2,
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String txt1,
            @RequestParam(required = false) String txt2) {

        FindByMedicineChartDto findByMedicineChartDto = new FindByMedicineChartDto();
        findByMedicineChartDto.setColor1(color1);
        findByMedicineChartDto.setColor2(color2);
        findByMedicineChartDto.setShape(shape);
        findByMedicineChartDto.setTxt1(txt1);
        findByMedicineChartDto.setTxt2(txt2);

        SuccessResponse<List<Medicine>> response = this.medicineService.getMedicine(findByMedicineChartDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
