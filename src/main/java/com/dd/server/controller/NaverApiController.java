package com.dd.server.controller;

import com.dd.server.dto.NaverApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NaverApiController {
    // Naver api url
    private static final String NAVER_URL = "openapi.naver.com/";
    private static final String API_PATH = "v1/search/kin.json";
    private static final String KEY = "Oefp2BwO3NZXAOY7w1Hh";
    private static final String SECRET_KEY = "Rw1F3J_aga";

    protected static final Logger logger = LoggerFactory.getLogger(NaverApiController.class);


    protected final WebClient webClient = WebClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("X-Naver-Client-Id", KEY)
            .defaultHeader("X-Naver-Client-Secret", SECRET_KEY)
            .build();

    public Mono<NaverApiResponseDto> send(String query){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("HTTPS")
                .host(NAVER_URL)
                .path(API_PATH)
                .queryParam("query",query);

        String uri = uriBuilder.build().toUriString();

        logger.info("Request URL : {}", uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NaverApiResponseDto.class)
                .doOnNext(response -> logger.info("Response : {}", response));
    }
}
