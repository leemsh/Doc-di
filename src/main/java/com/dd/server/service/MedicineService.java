package com.dd.server.service;

import com.dd.server.converter.MedicineConverter;
import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.MedicineResponse;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.MedicineRepository;
import com.dd.server.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MedicineApiService medicineApiService;

//    @Autowired
//    public MedicineService(MedicineApiService medicineApiService){
//        this.medicineApiService = medicineApiService;
//        this.medicineRepository
//    }

    public SuccessResponse getMedicine(FindByMedicineChartDto findByMedicineChartDto) {
        // DTO에서 값을 추출
        String color1 = findByMedicineChartDto.getColor1();
        String color2 = findByMedicineChartDto.getColor2();
        String shape = findByMedicineChartDto.getShape();
        String txt1 = findByMedicineChartDto.getTxt1();
        String txt2 = findByMedicineChartDto.getTxt2();

        logger.info("Received request with parameters: color1={}, color2={}, shape={}, txt1={}, txt2={}",
                color1, color2, shape, txt1, txt2);

        // API 요청
        MedicineResponse medicineResponse = medicineApiService.getMedicineFromApi(findByMedicineChartDto).block();
        // color2, shape, txt1, txt2도 API 요청에 추가해야 합니다 (추가 로직 필요)

        if (medicineResponse == null || medicineResponse.getBody() == null || medicineResponse.getBody().getItems() == null) {
            logger.warn("No data received from API or response body is null");
            return new SuccessResponse(false, "No data received from API or response body is null");
        }

        logger.info("Received medicine response from API: {}", medicineResponse);

        // medicineResponse를 MySQL DB에 저장하기 (중복 확인하기)
        for (MedicineResponse.Item item : medicineResponse.getBody().getItems()) {
            Optional<Medicine> existingMedicine = medicineRepository.findByItemSeq(item.getItemSeq());
            if (existingMedicine.isEmpty()) {
                Medicine medicine = MedicineConverter.toEntity(item);
                medicineRepository.save(medicine);
                logger.info("Saved new medicine to database: {}", medicine.getItemName());
            } else {
                logger.info("Medicine already exists in database: {}", existingMedicine.get());
            }
        }

        // MySQL DB에서 찾아오기
        Optional<Medicine> optionalMedicine = medicineRepository.findByChartMedicine(color1, color2, shape, txt1, txt2);
        if (optionalMedicine.isPresent()) {
            logger.info("Found medicine in database: {}", optionalMedicine.get());
        } else {
            logger.warn("No medicine found in database for given criteria");
        }

        return new SuccessResponse(true, optionalMedicine.orElse(null));
    }
}
