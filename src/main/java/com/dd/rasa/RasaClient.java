package com.dd.rasa;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RasaClient {
    // Rasa 서버 URL
    private static final String RASA_URL = "http://localhost:5015/webhooks/rest/webhook";

    // HttpClient 인스턴스
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void send(String user, String message) {
        try {
            // JSON 형식의 요청 데이터 생성
            Map<String, String> requestData = new HashMap<>();
            requestData.put("sender", user);
            requestData.put("message", message);

            // ObjectMapper를 사용하여 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(requestData);

            // HttpRequest 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RASA_URL))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();

            // 요청을 보내고 응답 받기
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // 응답 코드 확인
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                // 응답 본문 출력
                System.out.println("Rasa 응답: " + response.body());
            } else {
                System.out.println("Error: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}