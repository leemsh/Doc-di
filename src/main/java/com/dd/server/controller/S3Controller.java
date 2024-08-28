package com.dd.server.controller;

import com.dd.server.dto.DataDto;
import com.dd.server.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @GetMapping(path = "/data/{fileName}")
    public ResponseEntity<byte[]> getPetImage(
            @PathVariable String fileName
    ) throws IOException {
        return s3Service.download(fileName);
    }
}