package com.dd.server.service;

import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.repository.MedicineRepository;
import com.dd.server.domain.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;

    /*
    * @description 알약 검색 컨트롤러
    * @path /medicine/find
    * @return Medicine
     */

    public SuccessResponse getMedicine(FindByMedicineChartDto findByMedicineChartDto) {
        // API 요청해서 저장하기
        Medicine byChartMedicine = this.medicineRepository.findByChartMedicine(findByMedicineChartDto);

        return new SuccessResponse(true, byChartMedicine);
    }
}
