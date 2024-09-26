package com.dd.server.service;

import com.dd.server.controller.RasaController;
import com.dd.server.dto.ChatBotClientDto;
import com.dd.server.dto.RasaDto;
import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final MedicineService medicineService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse chatWithRasa(ChatBotClientDto chatBotClientDto) {
        RasaDto rasaDto = new RasaDto();
        try {
            rasaDto = RasaController.send(chatBotClientDto).block();
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("Rasa API Failed", 500);
        }
        assert rasaDto != null;
        if(Objects.equals(rasaDto.getStatus(), 200)){
            if(Objects.equals(rasaDto.getAction(), "DB_SEARCH")){
                FindByMedicineChartDto findByMedicineChartDto = convertStringToMedicineChartDto(rasaDto.getData());
                return new SuccessResponse(medicineService.getMedicine(findByMedicineChartDto),200);
            }

            // 다른 ACTION 들 생기면 추가 가능


            if(Objects.equals(rasaDto.getAction(), "PLAIN")){
                return new SuccessResponse<>(rasaDto.getData(), 200);
            }
        }
        return new SuccessResponse<>("Rasa ChatBot server interner error", rasaDto.getStatus());
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
