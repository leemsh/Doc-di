package com.dd.server.service;

import com.dd.server.controller.GeminiController;
import com.dd.server.controller.NaverApiController;
import com.dd.server.controller.RasaController;
import com.dd.server.domain.Medicine;
import com.dd.server.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final MedicineService medicineService;
    private final RasaController rasaController;
    private final NaverApiController naverApiController;
    private final GeminiController geminiController;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse chatWithRasa(ChatBotClientDto chatBotClientDto) {
        RasaDto rasaDto;
        try {
            rasaDto = rasaController.send(chatBotClientDto).block();
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("Rasa API Failed", 500);
        }
        assert rasaDto != null;

        RasaCustomDto rasaCustomDto = rasaDto.getCustom();

        //알약 검색
        if(Objects.equals(rasaCustomDto.getAction(), "DB_SEARCH")){
            List<Medicine> medicine = medicineService.getMedicine(rasaCustomDto.getData()).getData();
            rasaDto.setMedicineList(medicine);
        }

        //Naver API 검색
        if(Objects.equals(rasaCustomDto.getAction(), "WEB_SEARCH")){
            //Naver API 로 받아옴
            NaverApiResponseDto data = naverApiController.send(rasaCustomDto.getData().getQuery()).block();
            GeminiDto geminiDto = new GeminiDto();
            geminiDto.setSender(chatBotClientDto.getSender());
            geminiDto.setQuery(rasaCustomDto.getData().getQuery());

            if(data==null) rasaDto.setText("죄송합니다 네이버 지식인 검색에 실패하였습니다.");
            else if(data.getTotal()>0) {
                //Naver API 로 받아온 데이터를 가공하여 Gemini에 보낼 수 있도록 세팅(검색결과 최대 10개)
                List<GeminiSenderDataDto> geminiSenderDataDtos = new ArrayList<>();

                for (NaverSearchItemDto naverSearchItemDto : data.getItems()) {
                    GeminiSenderDataDto geminiSenderDataDto = new GeminiSenderDataDto();
                    geminiSenderDataDto.setTitle(naverSearchItemDto.getTitle());
                    geminiSenderDataDto.setLink(naverSearchItemDto.getLink());
                    geminiSenderDataDtos.add(geminiSenderDataDto);
                }
                geminiDto.setData(geminiSenderDataDtos);

                try {
                    rasaDto = geminiController.send(geminiDto).block();
                    rasaDto.setCustom(rasaCustomDto);

                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                    RasaDto tempRasaDto = new RasaDto();
                    tempRasaDto.setText("Sorry Gemini summary has been failed");
                }
            }
            //Naver API 로 받아온 데이터가 없을 시 Gemini에 문서요약 요청 스킵 후 검색결과 없음 작성
            else{
                rasaDto.setText("Sorry there is No Search Result");
            }
        }
        return new SuccessResponse<>(rasaDto, 200);
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
