package com.dd.rasa.controller;


import com.dd.rasa.dto.ChatBotClientDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.rasa.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping(value = "/chatbot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> chatWithRasa(@RequestBody ChatBotClientDto chatBotClientDto) {
        SuccessResponse<String> response = chatBotService.chatWithRasa(chatBotClientDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
