package com.dd.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class MedicineResponse {

    @JsonProperty("header")
    private Header header;

    @JsonProperty("body")
    private Body body;

    // Getters and Setters

    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;

        // Getters and Setters
    }

    public static class Body {
        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("items")
        private List<Item> items;

        // Getters and Setters
    }

    public static class Item {
        @JsonProperty("ITEM_SEQ")
        private String itemSeq;

        @JsonProperty("ITEM_NAME")
        private String itemName;

        @JsonProperty("ENTP_SEQ")
        private String entpSeq;

        @JsonProperty("ENTP_NAME")
        private String entpName;

        @JsonProperty("CHART")
        private String chart;

        @JsonProperty("ITEM_IMAGE")
        private String itemImage;

        @JsonProperty("PRINT_FRONT")
        private String printFront;

        @JsonProperty("PRINT_BACK")
        private String printBack;

        @JsonProperty("DRUG_SHAPE")
        private String drugShape;

        @JsonProperty("COLOR_CLASS1")
        private String colorClass1;

        @JsonProperty("COLOR_CLASS2")
        private String colorClass2;

        @JsonProperty("LINE_FRONT")
        private String lineFront;

        @JsonProperty("LINE_BACK")
        private String lineBack;

        @JsonProperty("LENG_LONG")
        private String lengLong;

        @JsonProperty("LENG_SHORT")
        private String lengShort;

        @JsonProperty("THICK")
        private String thick;

        @JsonProperty("IMG_REGIST_TS")
        private String imgRegistTs;

        @JsonProperty("CLASS_NO")
        private String classNo;

        @JsonProperty("CLASS_NAME")
        private String className;

        @JsonProperty("ETC_OTC_NAME")
        private String etcOtcName;

        @JsonProperty("ITEM_PERMIT_DATE")
        private String itemPermitDate;

        @JsonProperty("FORM_CODE_NAME")
        private String formCodeName;

        @JsonProperty("MARK_CODE_FRONT_ANAL")
        private String markCodeFrontAnal;

        @JsonProperty("MARK_CODE_BACK_ANAL")
        private String markCodeBackAnal;

        @JsonProperty("MARK_CODE_FRONT_IMG")
        private String markCodeFrontImg;

        @JsonProperty("MARK_CODE_BACK_IMG")
        private String markCodeBackImg;

        @JsonProperty("ITEM_ENG_NAME")
        private String itemEngName;

        @JsonProperty("CHANGE_DATE")
        private String changeDate;

        @JsonProperty("MARK_CODE_FRONT")
        private String markCodeFront;

        @JsonProperty("MARK_CODE_BACK")
        private String markCodeBack;

        @JsonProperty("EDI_CODE")
        private String ediCode;

        @JsonProperty("BIZRNO")
        private String bizrno;

        // Getters and Setters

    }
}
