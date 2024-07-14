package com.dd.server.repository;

import com.dd.server.domain.Medicine;
import com.dd.server.dto.FindByMedicineChartDto;
import org.springframework.stereotype.Repository;

@Repository
public class MedicineRepository {

    public Medicine findByChartMedicine(FindByMedicineChartDto findByMedicineChartDto){
        Medicine medicine = null;
        //이거 아무거나 한거라서 나중에 지워야함
        medicine.color1="red";

        // 그리고 여기서 findMyMedicineChartDto 값들을 이용해서 DB에서 검색 할것
        return medicine;
    }

}
