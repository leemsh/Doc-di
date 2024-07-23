package com.dd.server.converter;

import com.dd.server.domain.Medicine;
import com.dd.server.dto.MedicineResponse;

public class MedicineConverter {

    public static Medicine toEntity(MedicineResponse.Item item) {
        Medicine medicine = new Medicine();
        medicine.setItemName(item.getItemName());
        medicine.setEntpSeq(item.getEntpSeq());
        medicine.setEntpName(item.getEntpName());
        medicine.setChart(item.getChart());
        medicine.setItemImage(item.getItemImage());
        medicine.setPrintFront(item.getPrintFront());
        medicine.setPrintBack(item.getPrintBack());
        medicine.setDrugShape(item.getDrugShape());
        medicine.setColorClass1(item.getColorClass1());
        medicine.setColorClass2(item.getColorClass2());
        medicine.setLineFront(item.getLineFront());
        medicine.setLineBack(item.getLineBack());
        medicine.setLengLong(item.getLengLong());
        medicine.setLengShort(item.getLengShort());
        medicine.setThick(item.getThick());
        medicine.setImgRegistTs(item.getImgRegistTs());
        medicine.setClassNo(item.getClassNo());
        medicine.setClassName(item.getClassName());
        medicine.setEtcOtcName(item.getEtcOtcName());
        medicine.setItemPermitDate(item.getItemPermitDate());
        medicine.setFormCodeName(item.getFormCodeName());
        medicine.setMarkCodeFrontAnal(item.getMarkCodeFrontAnal());
        medicine.setMarkCodeBackAnal(item.getMarkCodeBackAnal());
        medicine.setMarkCodeFrontImg(item.getMarkCodeFrontImg());
        medicine.setMarkCodeBackImg(item.getMarkCodeBackImg());
        medicine.setItemEngName(item.getItemEngName());
        medicine.setChangeDate(item.getChangeDate());
        medicine.setMarkCodeFront(item.getMarkCodeFront());
        medicine.setMarkCodeBack(item.getMarkCodeBack());
        medicine.setEdiCode(item.getEdiCode());
        medicine.setBizrno(item.getBizrno());

        return medicine;
    }
}
