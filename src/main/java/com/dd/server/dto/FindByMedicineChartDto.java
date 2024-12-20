package com.dd.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindByMedicineChartDto {
    public String name; //이름
    public String color1; //색1
    public String color2; //색2
    public String shape; // 제형
    public String txt1; //글자1
    public String txt2; //글자2
    public String query; // 검색용 쿼리
}
