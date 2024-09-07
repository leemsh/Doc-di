package com.dd.server.service;

import com.dd.model.PillPredictor;
import com.dd.server.controller.MedicineApiController;
import com.dd.server.controller.MedicineInfoApiController;
import com.dd.server.converter.MedicineConverter;
import com.dd.server.dto.*;
import com.dd.server.repository.MedicineRepository;
import com.dd.server.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MedicineApiController medicineApiController;

    private final MedicineInfoApiController medicineInfoApiController;


    public SuccessResponse<List<Medicine>> getMedicine(FindByMedicineChartDto findByMedicineChartDto) {
        // DTO에서 값을 추출
        String name = findByMedicineChartDto.getName();
        String color1 = findByMedicineChartDto.getColor1();
        String color2 = findByMedicineChartDto.getColor2();
        String shape = findByMedicineChartDto.getShape();
        String txt1 = findByMedicineChartDto.getTxt1();
        String txt2 = findByMedicineChartDto.getTxt2();

        logger.info("Received request with parameters: name={}, color1={}, color2={}, shape={}, txt1={}, txt2={}",
               name, color1, color2, shape, txt1, txt2);


        // API 요청
        MedicineResponse medicineResponse = medicineApiController.getMedicineFromApi(findByMedicineChartDto).block();
        logger.info("Received medicine response from API: {}", medicineResponse);


        // medicineResponse를 MySQL DB에 저장하기 (중복 확인하기)
        if(medicineResponse.getBody()!=null){
            for (MedicineResponse.Item item : medicineResponse.getBody().getItems()) {
                Medicine existingMedicine = medicineRepository.findByItemName(item.getItemName());
                if (existingMedicine == null) {
                    Medicine medicine = MedicineConverter.toEntity(item);
                    medicineRepository.save(medicine);
                    logger.info("Saved new medicine to database: {}", medicine.getItemName());
                } else {
                    logger.info("Medicine already exists in database: {}", existingMedicine);
                }
            }
        }

        // MySQL DB에서 찾아오기
        List<Medicine> medicines;
        try {
            medicines = medicineRepository.findByChartMedicine(name, color1, color2, shape, txt1, txt2);
        } catch (Exception e) {
            return new SuccessResponse("DB error", 500);
        }
        if (!medicines.isEmpty()) {
            logger.info("Found medicine(s) in database: {}", medicines);
        } else {
            logger.warn("No medicine found in database for given criteria");
            return new SuccessResponse("No data received from API or response body is null", 204);
        }

        return new SuccessResponse<>(medicines, 200);
    }


    public SuccessResponse<MedicineInfoDto> getMedicineInfo(String itemName){
        logger.info("Received request with parameters: name={}", itemName);

        MedicineInfoResponse medicineInfoResponse = medicineInfoApiController.getMedicineInfoFormApi(itemName).block();
        if(medicineInfoResponse.getBody()!=null){
            MedicineInfoDto medicineInfoDto = MedicineConverter.infoToDto(medicineInfoResponse.getBody().getItems().get(0));
            return new SuccessResponse<>(medicineInfoDto, 200);
        }
        return new SuccessResponse("No data received from API or response body is null", 204);

    }

    public SuccessResponse<Medicine> putStatistics(MedicineStatisticsDto medicineStatisticsDto){
        Medicine medicine = medicineRepository.findByItemName(medicineStatisticsDto.getName());

        medicine.setRateTotal(medicineStatisticsDto.getRateTotal());
        medicine.setRateAmount(medicineStatisticsDto.getRateAmount());
        try{
            medicineRepository.save(medicine);
        }catch (Exception e){
            return new SuccessResponse("DB error", 500);
        }
        return new SuccessResponse<>(medicine, 200);
    }


    public SuccessResponse<List<Medicine>> getMedicineByImage(String filePath) throws IOException {
        return getMedicine(MedicineConverter.toDto(PillPredictor.predict(filePath)));
    }
}
