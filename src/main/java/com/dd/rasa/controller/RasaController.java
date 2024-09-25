package com.dd.rasa.controller;

import com.dd.rasa.dto.ChatBotClientDto;
import com.dd.rasa.dto.RasaDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RasaController {
    // Rasa 서버 URL
    private static final String RASA_URL = "http://localhost:5015/webhooks/rest/webhook";
    private static final Logger logger = LoggerFactory.getLogger(RasaController.class);

    // WebClient 인스턴스 (Spring의 WebClient를 사용)
    private static final WebClient webClient = WebClient.builder()
            .baseUrl(RASA_URL)
            .defaultHeader("Content-Type", "application/json")
            .build();

    public static Mono<RasaDto> send(ChatBotClientDto chatBotClientDto) {
        // WebClient를 통해 POST 요청 보내기
        return webClient.post()
                .bodyValue(chatBotClientDto) // Dto를 요청 본문으로 설정
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> {
                    logger.error("Error: Status Code {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Error: " + clientResponse.statusCode()));
                })
                .bodyToMono(RasaDto.class) // 응답을 RasaDto로 직접 변환
                .doOnNext(rasaDto -> logger.info("Rasa 응답: {}", rasaDto))
                .doOnError(e -> logger.error("Error occurred: {}", e.getMessage()));
    }
}
