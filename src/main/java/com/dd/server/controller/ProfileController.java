package com.dd.server.controller;

import com.dd.server.domain.Medicine;
import com.dd.server.domain.Profile;
import com.dd.server.domain.User;
import com.dd.server.dto.ProfileDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.dto.UserDto;
import com.dd.server.service.ProfileService;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Profile>> getProfile(@RequestParam String email) {
        Profile profile = profileService.getProfile(email);
        SuccessResponse<Profile> response;
        if(profile != null)
            response = new SuccessResponse<>(true, profile);
        else
            response = new SuccessResponse<>(false, null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<Profile>> getMedicineByImage(
            @RequestParam String email,
            @RequestParam String name,
            @RequestPart("image") MultipartFile imageFile) {

        if (imageFile.isEmpty()) {
            logger.error("Error: File is missing");
            throw new IllegalArgumentException("File is missing");
        }

        // 파일 저장 경로 지정 (서버의 "profile" 디렉토리)
        String uploadDir = "/opt/profile/";
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            logger.error("Error: Invalid file name {}", originalFilename);
            throw new IllegalArgumentException("Invalid file name");
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

            ProfileDto profileDto = new ProfileDto();
            profileDto.setEmail(email);
            profileDto.setName(name);
            profileDto.setImage(filePath);

            // 저장된 파일의 경로를 저장하는 profile 서비스 호출
            logger.info("Calling medicine service with file: {}", filePath);
            SuccessResponse<Profile> response = this.profileService.editProfile(profileDto);
            logger.info("profile service returned successfully.");

            // 응답 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("IOException occurred while processing file {}", originalFilename, e);
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}", originalFilename, e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> deleteUser(
            @RequestParam(required = true) String email){

        SuccessResponse<String> response;

        boolean isDelete = profileService.deleteProfile(email);
        if(isDelete)
            response = new SuccessResponse<>(true,"delete Complete");
        else
            response = new SuccessResponse<>(false,"delete failed");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
