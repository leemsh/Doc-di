package com.dd.server.service;

import com.dd.server.domain.User;
import com.dd.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final S3Service s3Service;


    public String createImage(String fileName, MultipartFile imageFile) {
        if(imageFile.isEmpty()) {
            return null;
        }
        // 파일 저장 경로 지정 (서버의 "profile" 디렉토리)
        String uploadDir = "profile";
        String filePath = uploadDir + '/' + fileName + ".jpg";
        String originalFilename = imageFile.getOriginalFilename();
        logger.info("File path: {}", filePath);

        try {
            // 파일 저장
            s3Service.upload(fileName, imageFile, "jpg", uploadDir);
            return filePath;
        } catch (IOException e) {
            logger.error("IOException occurred while processing file {}", originalFilename, e);
            return null;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}", originalFilename, e);
            return null;
        }
    }


    public String resetImage(String email, String fileName, MultipartFile imageFile) {

        User user = userRepository.findByEmail(email);
        // 원래 있던 파일 삭제
        if(user.getImage() != null) {
            logger.info("Deleting file: {}", user.getImage());
            try {
                s3Service.delete(user.getImage());
            }catch (Exception e){
                logger.error("An unexpected error occurred while deleting file in S3{}", fileName, e);
                return null;
            }
        }

        // 파일 저장 경로 지정 (서버의 "profile" 디렉토리)
        String uploadDir = "profile";
        String filePath = uploadDir + '/' + fileName + ".jpg";
        String originalFilename = imageFile.getOriginalFilename();
        logger.info("File path: {}", filePath);

        try {
            // 파일 저장
            s3Service.upload(fileName, imageFile, "jpg", uploadDir);
            logger.info("profile service returned successfully.");
            return filePath;
        } catch (IOException e) {
            logger.error("IOException occurred while processing file {}", originalFilename, e);
            return null;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while processing file {}", originalFilename, e);
            return null;
        }
    }

    public void deleteImage(String email) {
        try {
            s3Service.delete(userRepository.findByEmail(email).getImage());
            logger.info("delete success in S3");
        }catch (Exception e){
            logger.error("An unexpected error occurred while deleting file in S3{}", email, e);
        }
    }
}
