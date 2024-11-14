package com.dd.server.controller;

import com.dd.server.domain.Medicine;
import com.dd.server.dto.*;
import com.dd.server.service.MedicineService;
import com.dd.server.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;
    private final SearchHistoryService searchHistoryService;
    private final PillPredictController pillPredictController;

    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicine(@RequestBody FindByMedicineChartDto findByMedicineChartDto) {
        SuccessResponse<List<Medicine>> response = this.medicineService.getMedicine(findByMedicineChartDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }



    @PostMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<MedicineInfoDto>> getMedicineInfo(@RequestBody SearchHistoryDto searchHistoryDto) {
        SuccessResponse<MedicineInfoDto> response = this.medicineService.getMedicineInfo(searchHistoryDto.getItemSeq());
        searchHistoryService.createHistory(searchHistoryDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Medicine>> putMedicineStatistics(@RequestBody MedicineStatisticsDto medicineStatisticsDto){
        SuccessResponse<Medicine> response = this.medicineService.putStatistics(medicineStatisticsDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }


    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicineByImage(
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        if (imageFile.isEmpty()) {
            logger.error("Error: File is missing");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse response = new SuccessResponse("Error: File is missing", 400);
            return new ResponseEntity<>(response, headers, response.getStatus());
        }

        List<PillPredictReceiveDto> receiveDtoList = pillPredictController.send(imageFile).block();
        if (receiveDtoList == null || receiveDtoList.isEmpty()) {
            logger.error("Error: PillPredictReceiveDto is null or empty");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<List<Medicine>> response = new SuccessResponse("Error: PillPredictReceiveDto is null or empty", 500);
            return new ResponseEntity<>(response, headers, 500);
        }

        List<Medicine> medicineResponse = new ArrayList<>();
        for(PillPredictReceiveDto receiveDto : receiveDtoList){
            FindByMedicineChartDto findByMedicineChartDto = new FindByMedicineChartDto();
            if (receiveDto.getColor() != null) {
                if (receiveDto.getColor().size() == 1) {
                    findByMedicineChartDto.setColor1(receiveDto.getColor().get(0));
                } else {
                    findByMedicineChartDto.setColor1(receiveDto.getColor().get(0));
                    findByMedicineChartDto.setColor2(receiveDto.getColor().get(1));
                }
            }
            if (receiveDto.getShape() != null) {
                findByMedicineChartDto.setShape(receiveDto.getShape().get(0));
            }
            SuccessResponse<List<Medicine>> tempResponse = medicineService.getMedicine(findByMedicineChartDto);
            if(tempResponse.getStatus() == 200) medicineResponse.addAll(tempResponse.getData());
            logger.info("info:medicine:{}", tempResponse.getData());
        }
        SuccessResponse<List<Medicine>> response;
        if(!medicineResponse.isEmpty()) response= new SuccessResponse<>(medicineResponse, 200);
        else response= new SuccessResponse("No medicine found in database for given criteria", 200);

        // 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
