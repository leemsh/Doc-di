package com.dd.server.controller;

import com.dd.server.dto.JoinDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.JoinService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }
    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<JoinDto>> joinProcess(
            @RequestPart JoinDto joinDto,
            @RequestPart MultipartFile file){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SuccessResponse<JoinDto> response = joinService.joinProcess(joinDto, file);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
