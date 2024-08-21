package com.dd.server.controller;

import com.dd.server.domain.Medicine;
import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.MedicineInfoDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    /*
    * 알약 검색 컨트롤러
    * path : /medicine/find
    * return : 약에 대한 정보
     */

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicine(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color1,
            @RequestParam(required = false) String color2,
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String txt1,
            @RequestParam(required = false) String txt2) {

        FindByMedicineChartDto findByMedicineChartDto = new FindByMedicineChartDto();
        findByMedicineChartDto.setName(name);
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



    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<MedicineInfoDto>> getMedicineInfo(
            @RequestParam String name){

        SuccessResponse<MedicineInfoDto> response = this.medicineService.getMedicineInfo(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<List<Medicine>>> getMedicineByImage(
            @RequestPart("image") MultipartFile imageFile) {

        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("File is missing");
        }

        // 파일 저장 경로 지정 (예: 서버의 "uploads" 디렉토리)
        String uploadDir = "/opt/uploads/";
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        String filePath = uploadDir + originalFilename;

        try {
            // 업로드 디렉토리 생성 (존재하지 않을 경우)
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            File destinationFile = new File(filePath);
            imageFile.transferTo(destinationFile);

            // 저장된 파일의 경로를 기반으로 Medicine 정보를 가져오는 서비스 호출
            SuccessResponse<List<Medicine>> response = this.medicineService.getMedicineByImage(filePath);

            // 파일 삭제
            boolean isDeleted = destinationFile.delete();
            if (!isDeleted) {
                // 로깅 추가
                System.err.println("Failed to delete file " + originalFilename);
                // 예외를 그대로 던지거나 적절한 예외 처리
                throw new RuntimeException("Failed to delete file " + originalFilename);
            }

            // 응답 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        } catch (IOException e) {
            // 파일 저장 중 예외 처리
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        } catch (Exception e) {
            // 모든 예외 처리
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

}
