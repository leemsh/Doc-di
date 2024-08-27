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
            @RequestParam String name,
            @RequestPart("image") MultipartFile imageFile) {

        if (imageFile.isEmpty()) {
            logger.error("Error: File is missing");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<Profile> response = new SuccessResponse("Error: File is missing", 400);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }

        Profile existedProfile = profileRepository.findByEmail(email);
        if(existedProfile.getImage()!=null){
            // 원래 있던 파일 삭제
            logger.info("Deleting file: {}", existedProfile.getImage());

            File file = new File(existedProfile.getImage());
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                logger.error("Failed to delete file {}", existedProfile.getImage());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                SuccessResponse<Profile> response = new SuccessResponse("Failed to delete existing file" + existedProfile.getImage(), 500);
                return new ResponseEntity<> (response, headers, response.getStatus());
            } else {
                logger.info("File deleted successfully: {}", existedProfile.getImage());
            }
        }

        // 파일 저장 경로 지정 (서버의 "profile" 디렉토리)
        String uploadDir = "uploads/profile/";
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            logger.error("Error: Invalid file name {}", originalFilename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SuccessResponse<Profile> response = new SuccessResponse("Error: Invalid file name" + originalFilename, 400);
            return new ResponseEntity<> (response, headers, response.getStatus());
        }
        String filePath = uploadDir + originalFilename;

        logger.info("Upload directory: {}", uploadDir);
        logger.info("File path: {}", filePath);

        try {
            // 업로드 디렉토리 생성 (존재하지 않을 경우)
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                logger.info("Directory does not exist. Creating directory: {}", uploadPath);
                Files.createDirectories(uploadPath);
                logger.info("Directory created successfully.");
            } else {
                logger.info("Directory already exists: {}", uploadPath);
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
            logger.info("Calling profile service with file: {}", filePath);
            SuccessResponse<Profile> response = this.profileService.editProfile(profileDto);
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
            @RequestParam(required = true) String email){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SuccessResponse<String> response = profileService.deleteProfile(email);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
