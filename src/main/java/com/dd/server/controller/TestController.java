package com.dd.server.controller;

import com.dd.server.dto.NaverApiResponseDto;
import com.dd.server.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//이 컨트롤러는 네이버 API 테스트 용도입니다.
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {
    private final NaverApiController naverApiController;

    @PostMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<NaverApiResponseDto>> test(@RequestParam String query) {
        SuccessResponse<NaverApiResponseDto> response;
        try{
            NaverApiResponseDto data = naverApiController.send(query).block();
            response = new SuccessResponse<>(data,200);
        }catch (Exception e){
            e.printStackTrace();
            response = new SuccessResponse("fail", 500);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
