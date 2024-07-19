package com.dd.server.service;

import com.dd.server.controller.MedicineApiDataController;
import com.dd.server.controller.MedicineController;
import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.MedicineResponse;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.MedicineRepository;
import com.dd.server.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineApiDataController medicineApiDataController;

    /*
    * @description 알약 검색 컨트롤러
    * @path /medicine/find
    * @return Medicine
     */

    public SuccessResponse getMedicine(FindByMedicineChartDto findByMedicineChartDto) {
        // API 요청해서 저장하기
        MedicineResponse medicineResponse = medicineApiDataController.getMedicineEntity(findByMedicineChartDto.color1);



        //medicineResponse mySQL DB에 저장하기 (중복확인하기)
        //TODO

        //mysql DB에서 찾아오기
        Medicine byChartMedicine = this.medicineRepository.findByChartMedicine(findByMedicineChartDto);

        return new SuccessResponse(true, byChartMedicine);
    }
}
