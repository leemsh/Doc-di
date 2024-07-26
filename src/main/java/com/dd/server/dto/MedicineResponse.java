package com.dd.server.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineResponse {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;

        @JacksonXmlProperty(localName = "items")
        @JacksonXmlElementWrapper(useWrapping = true)
        private List<Item> items;
    }

    @Getter
    @Setter
    //@JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JacksonXmlProperty(localName = "ITEM_SEQ")
        private Long itemSeq;

        @JacksonXmlProperty(localName = "ITEM_NAME")
        private String itemName;

        @JacksonXmlProperty(localName = "ENTP_SEQ")
        private String entpSeq;

        @JacksonXmlProperty(localName = "ENTP_NAME")
        private String entpName;

        @JacksonXmlProperty(localName = "CHART")
        private String chart;

        @JacksonXmlProperty(localName = "ITEM_IMAGE")
        private String itemImage;

        @JacksonXmlProperty(localName = "PRINT_FRONT")
        private String printFront;

        @JacksonXmlProperty(localName = "PRINT_BACK")
        private String printBack;

        @JacksonXmlProperty(localName = "DRUG_SHAPE")
        private String drugShape;

        @JacksonXmlProperty(localName = "COLOR_CLASS1")
        private String colorClass1;

        @JacksonXmlProperty(localName = "COLOR_CLASS2")
        private String colorClass2;

        @JacksonXmlProperty(localName = "LINE_FRONT")
        private String lineFront;

        @JacksonXmlProperty(localName = "LINE_BACK")
        private String lineBack;

        @JacksonXmlProperty(localName = "LENG_LONG")
        private String lengLong;

        @JacksonXmlProperty(localName = "LENG_SHORT")
        private String lengShort;

        @JacksonXmlProperty(localName = "THICK")
        private String thick;

        @JacksonXmlProperty(localName = "IMG_REGIST_TS")
        private String imgRegistTs;

        @JacksonXmlProperty(localName = "CLASS_NO")
        private String classNo;

        @JacksonXmlProperty(localName = "CLASS_NAME")
        private String className;

        @JacksonXmlProperty(localName = "ETC_OTC_NAME")
        private String etcOtcName;

        @JacksonXmlProperty(localName = "ITEM_PERMIT_DATE")
        private String itemPermitDate;

        @JacksonXmlProperty(localName = "FORM_CODE_NAME")
        private String formCodeName;

        @JacksonXmlProperty(localName = "MARK_CODE_FRONT_ANAL")
        private String markCodeFrontAnal;

        @JacksonXmlProperty(localName = "MARK_CODE_BACK_ANAL")
        private String markCodeBackAnal;

        @JacksonXmlProperty(localName = "MARK_CODE_FRONT_IMG")
        private String markCodeFrontImg;

        @JacksonXmlProperty(localName = "MARK_CODE_BACK_IMG")
        private String markCodeBackImg;

        @JacksonXmlProperty(localName = "ITEM_ENG_NAME")
        private String itemEngName;

        @JacksonXmlProperty(localName = "CHANGE_DATE")
        private String changeDate;

        @JacksonXmlProperty(localName = "MARK_CODE_FRONT")
        private String markCodeFront;

        @JacksonXmlProperty(localName = "MARK_CODE_BACK")
        private String markCodeBack;

        @JacksonXmlProperty(localName = "EDI_CODE")
        private String ediCode;

        @JacksonXmlProperty(localName = "BIZRNO")
        private String bizrno;
    }
}
