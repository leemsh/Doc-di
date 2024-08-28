package com.dd.server.controller;

import com.dd.server.domain.Medicine;
import com.dd.server.domain.Profile;
import com.dd.server.domain.User;
import com.dd.server.dto.ProfileDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.dto.UserDto;
import com.dd.server.repository.ProfileRepository;
import com.dd.server.service.MedicineService;
import com.dd.server.service.ProfileService;
import com.dd.server.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final S3Service s3Service;

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Profile>> getProfile(@RequestParam String email) {
        SuccessResponse<Profile> response = profileService.getProfile(email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Profile>> getMedicineByImage(
            @RequestParam String email,
            @RequestPart("image") MultipartFile imageFile) {


        Profile existedProfile = profileRepository.findByEmail(email);
        // 원래 있던 파일 삭제
        logger.info("Deleting file: {}", existedProfile.getImage());
        try {
            s3Service.delete(existedProfile.getImage());
        }catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<Profile> response = new SuccessResponse("delete fail in S3 " + existedProfile.getImage(), 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
        // 파일 저장 경로 지정 (서버의 "profile" 디렉토리)
        String uploadDir = "profile";
        String originalFilename = imageFile.getOriginalFilename();
        String filePath = uploadDir + '/' + originalFilename;
        logger.info("File path: {}", filePath);

        try {
            // 파일 저장
            String uploadPath = s3Service.upload(imageFile.getName(), imageFile, "jpg", uploadDir);


            // 저장된 파일의 경로를 저장하는 profile 서비스 호출
            logger.info("Calling profile service with file: {}", filePath);
            SuccessResponse<Profile> response = this.profileService.editProfile(email, uploadPath);
            logger.info("profile service returned successfully.");

            // 응답 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, response.getStatus());

        } catch (IOException e) {
            logger.error("IOException occurred while processing file {}", originalFilename, e);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<Profile> response = new SuccessResponse("IOException occurred while processing file " +  originalFilename, 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}", originalFilename, e);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<Profile> response = new SuccessResponse("An unexpected error occurred while processing file " + originalFilename, 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> deleteUser(
            @RequestParam(required = true) String email) {
        try {
            s3Service.delete(profileRepository.findByEmail(email).getImage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<String> response = new SuccessResponse("delete success in S3 ", 200);
            return new ResponseEntity<>(response, headers, response.getStatus());

        }catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<String> response = new SuccessResponse("delete fail in S3 ", 500);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
    }
}
