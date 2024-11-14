package com.dd.server.controller;

import com.dd.server.dto.ChatBotClientDto;
import com.dd.server.dto.GeminiDto;
import com.dd.server.dto.RasaDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiController {
    // Rasa 서버 URL
    private static final String RASA_URL = "http://147.185.221.23:36902/";
    private static final String MESSAGE_PATH = "sum";
    protected static final Logger logger = LoggerFactory.getLogger(GeminiController.class);

    // WebClient 인스턴스 (Spring의 WebClient를 사용)
    protected final WebClient webClient = WebClient.builder()
            .baseUrl(RASA_URL + MESSAGE_PATH)
            .defaultHeader("Content-Type", "application/json")
            .build();

    public Mono<RasaDto> send(GeminiDto geminiDto) {
        return webClient.post()
                .bodyValue(geminiDto)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> {
                    logger.error("Error: Status Code {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Error: " + clientResponse.statusCode()));
                })
                .bodyToMono(new ParameterizedTypeReference<RasaDto>() {})
                .doOnNext(rasaDtoList -> logger.info("Rasa 응답: {}", rasaDtoList))
                .doOnError(e -> logger.error("Error occurred: {}", e.getMessage()));
    }
}
