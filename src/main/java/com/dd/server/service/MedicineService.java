package com.dd.server.service;

import com.dd.server.controller.MedicineApiDataController;
import com.dd.server.converter.MedicineConverter;
import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.MedicineResponse;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.MedicineRepository;
import com.dd.server.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineApiDataController medicineApiDataController;

    public SuccessResponse getMedicine(FindByMedicineChartDto findByMedicineChartDto) {
        // DTO에서 값을 추출
        String color1 = findByMedicineChartDto.getColor1();
        String color2 = findByMedicineChartDto.getColor2();
        String shape = findByMedicineChartDto.getShape();
        String txt1 = findByMedicineChartDto.getTxt1();
        String txt2 = findByMedicineChartDto.getTxt2();

        // API 요청
        MedicineResponse medicineResponse = medicineApiDataController.getMedicineEntity(color1);
        //color2 shape txt1 txt2도 다 해야함


        if (medicineResponse == null || medicineResponse.getBody() == null || medicineResponse.getBody().getItems() == null) {
            return new SuccessResponse(false, "No data received from API or response body is null");
        }

        // medicineResponse를 MySQL DB에 저장하기 (중복 확인하기)
        for (MedicineResponse.Item item : medicineResponse.getBody().getItems()) {
            Optional<Medicine> existingMedicine = medicineRepository.findByItemSeq(item.getItemSeq());
            if (existingMedicine.isEmpty()) {
                Medicine medicine = MedicineConverter.toEntity(item);
                medicineRepository.save(medicine);
            }
        }

        // MySQL DB에서 찾아오기
        Optional<Medicine> optionalMedicine = medicineRepository.findByChartMedicine(color1, color2, shape, txt1, txt2);

        return new SuccessResponse(true, optionalMedicine.orElse(null));
    }
}
