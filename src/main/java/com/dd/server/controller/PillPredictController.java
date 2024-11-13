package com.dd.server.controller;

import com.dd.server.dto.PillPredictReceiveDto;
import com.dd.server.dto.RasaDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PillPredictController {
    // Rasa 서버 URL
    private static final String PILL_URL = "http://147.185.221.23:36902/";
    private static final String MESSAGE_PATH = "pill";
    protected static final Logger logger = LoggerFactory.getLogger(PillPredictController.class);

    // WebClient 인스턴스 (Spring의 WebClient를 사용)
    protected final WebClient webClient = WebClient.builder()
            .baseUrl(PILL_URL + MESSAGE_PATH)
            .defaultHeader("Content-Type", "image/jpg")
            .build();

    public Flux<PillPredictReceiveDto> send(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();

        return webClient.post()
                .bodyValue(imageBytes)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> {
                    logger.error("Error: Status Code {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Error: " + clientResponse.statusCode()));
                })
                .bodyToFlux(new ParameterizedTypeReference<PillPredictReceiveDto>() {})
                .doOnNext(pillPredictReceiveDto -> logger.info("PillPredict 응답: {}", pillPredictReceiveDto))
                .doOnError(e -> logger.error("Error occurred: {}", e.getMessage()));
    }
}
