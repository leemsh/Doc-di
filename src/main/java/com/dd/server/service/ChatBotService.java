package com.dd.server.service;

import com.dd.rasa.RasaClient;
import com.dd.server.dto.ChatBotClientDto;
import com.dd.server.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotService {


    public SuccessResponse<String> chatWithRasa(ChatBotClientDto chatBotClientDto) {

        //TODO 완성하기
        RasaClient.send(chatBotClientDto.getEmail(), chatBotClientDto.getMessage());
        return new SuccessResponse<>("asdfadsf", 200);
    }
}
