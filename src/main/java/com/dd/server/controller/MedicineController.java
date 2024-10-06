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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;
    private final SearchHistoryService searchHistoryService;


    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicine(@RequestBody FindByMedicineChartDto findByMedicineChartDto) {
        SuccessResponse<List<Medicine>> response = this.medicineService.getMedicine(findByMedicineChartDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }



    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
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
            @RequestPart("image") MultipartFile imageFile) {

        if (imageFile.isEmpty()) {
            logger.error("Error: File is missing");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse response = new SuccessResponse("Error: File is missing", 400);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }

        // 파일 저장 경로 지정 (예: 서버의 "uploads" 디렉토리)
        String uploadDir = System.getProperty("user.dir");
        //String uploadDir = "\\opt\\uploads\\";
        //String uploadDir = "C:\\Users\\leems\\Downloads\\opts";
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            logger.error("Error: Invalid file name {}", originalFilename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse response = new SuccessResponse("Error: Invalid file name", 400);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
        String filePath = uploadDir + originalFilename;

        logger.info("Upload directory: {}", uploadDir);
        logger.info("File path: {}", filePath);

        try {
            // 업로드 디렉토리 생성 (존재하지 않을 경우)
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                logger.info("Directory does not exist. Creating directory: {}", uploadPath.toString());
                Files.createDirectories(uploadPath);
                logger.info("Directory created successfully.");
            } else {
                logger.info("Directory already exists: {}", uploadPath.toString());
            }

            // 파일 저장
            File destinationFile = new File(filePath);
            logger.info("Saving file to: {}", filePath);
            imageFile.transferTo(destinationFile);
            logger.info("File saved successfully: {}", filePath);

            // 저장된 파일의 경로를 기반으로 Medicine 정보를 가져오는 서비스 호출
            logger.info("Calling medicine service with file: {}", filePath);
            SuccessResponse<List<Medicine>> response = this.medicineService.getMedicineByImage(filePath);
            logger.info("Medicine service returned successfully.");

            // 파일 삭제
            logger.info("Deleting file: {}", filePath);
            boolean isDeleted = destinationFile.delete();
            if (!isDeleted) {
                logger.error("Failed to delete file {}", originalFilename);
                response.setStatus(500);
                response.setMessage("Failed to delete file " + originalFilename);
            } else {
                logger.info("File deleted successfully: {}", filePath);
            }

            // 응답 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, response.getStatus());

        } catch (IOException e) {
            logger.error("IOException occurred while processing file {}", originalFilename, e);HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse response = new SuccessResponse("IOException occurred while processing file", 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}", originalFilename, e);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse response = new SuccessResponse("An unexpected error occurred while processing file", 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
    }

}
