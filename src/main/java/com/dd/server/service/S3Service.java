package com.dd.server.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
@Service
public class S3Service{
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    //파라미터 파일이름, 파일, 확장자, 디렉토리네임
    public String upload(String fileName, MultipartFile multipartFile, String extend, String dirName) throws IOException { // dirName의 디렉토리가 S3 Bucket 내부에 생성됨

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(fileName, uploadFile, extend, dirName);
    }

    private String upload(String fileName,File uploadFile, String extend, String dirName) {
        String newFileName = dirName + "/" + fileName+extend;
        String uploadImageUrl = putS3(uploadFile, newFileName);

        removeNewFile(uploadFile);  // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info(file.getOriginalFilename());
        File convertFile = new File(file.getOriginalFilename()); // 업로드한 파일의 이름
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public ResponseEntity<byte[]> download(String fullFilePath) {
        try {
            // S3에서 객체 가져오기
            S3Object awsS3Object = amazonS3.getObject(new GetObjectRequest(bucket, fullFilePath));
            S3ObjectInputStream s3is = awsS3Object.getObjectContent();
            byte[] bytes = s3is.readAllBytes();

            // 파일 이름 추출
            String fileName = fullFilePath.substring(fullFilePath.lastIndexOf("/") + 1);
            String downloadedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");

            // HTTP 헤더 설정
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", downloadedFileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        } catch (AmazonS3Exception e) {
            log.error("S3에서 파일을 다운로드하는 도중 오류가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.error("파일을 읽는 도중 오류가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("파일을 다운로드하는 도중 알 수 없는 오류가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // S3에서 파일 삭제하는 메서드
    public void delete(String fullFilePath) {

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fullFilePath));
            log.info("파일이 S3에서 삭제되었습니다. 경로: {}", fullFilePath);
        } catch (AmazonS3Exception e) {
            // S3 관련 예외 처리 (권한 문제, 파일 존재하지 않음 등)
            log.error("S3에서 파일을 삭제하는 도중 오류가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            throw new RuntimeException("S3 파일 삭제 실패: " + e.getMessage(), e);
        } catch (SdkClientException e) {
            // 네트워크 오류 등 S3 클라이언트 문제 처리
            log.error("S3 클라이언트에서 문제가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            throw new RuntimeException("S3 클라이언트 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            // 일반적인 예외 처리
            log.error("파일을 삭제하는 도중 알 수 없는 오류가 발생했습니다. 경로: {}. 오류: {}", fullFilePath, e.getMessage());
            throw new RuntimeException("알 수 없는 오류로 인한 S3 파일 삭제 실패: " + e.getMessage(), e);
        }
    }

}