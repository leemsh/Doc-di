package com.dd.server.service;

import com.dd.server.controller.RasaController;
import com.dd.server.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final MedicineService medicineService;
    private final RasaController rasaController;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse chatWithRasa(ChatBotClientDto chatBotClientDto) {
        List<RasaDto> listRasaDto;
        try {
            listRasaDto = rasaController.send(chatBotClientDto).block();
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("Rasa API Failed", 500);
        }
        assert listRasaDto != null;


        // 다른 ACTION 들 생기면 추가 가능

        return new SuccessResponse<>(listRasaDto, 200);
    }

    private FindByMedicineChartDto convertStringToMedicineChartDto(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 문자열을 FindByMedicineChartDto 객체로 변환
            return objectMapper.readValue(data, FindByMedicineChartDto.class);
        } catch (Exception e) {
            logger.error("Error converting JSON string to FindByMedicineChartDto: {}", e.getMessage());
            throw new RuntimeException("Failed to convert JSON to DTO", e);
        }
    }
}
