package com.dd.server.service;

public class MedicineService {
    private final MedicineRepository medicineRepository;

    /*
    * @description 알약 검색 컨트롤러
    * @path /medicine/find
    * @return Medicine
     */

    public SuccessResponse getMedicine(String color1, String color2, String shape, String txt1, String txt2){
        // API 요청해서 저장하기
        Medicine byChartMedicine = this.medicineRepository.findByChartMedicine(color1, color2, shape, txt1, txt2);

        return new SuccessResponse(true, byChartMedicine);
    }
}
