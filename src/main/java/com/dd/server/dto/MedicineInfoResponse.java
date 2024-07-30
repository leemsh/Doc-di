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
public class MedicineInfoResponse {

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
    public static class Item {
        @JacksonXmlProperty(localName = "entpName")
        private String entpName;

        @JacksonXmlProperty(localName = "itemName")
        private String itemName;

        @JacksonXmlProperty(localName = "itemSeq")
        private Long itemSeq;

        @JacksonXmlProperty(localName = "efcyQesitm")
        private String efcyQesitm;

        @JacksonXmlProperty(localName = "useMethodQesitm")
        private String useMethodQesitm;

        @JacksonXmlProperty(localName = "atpnWarnQesitm")
        private String atpnWarnQesitm;

        @JacksonXmlProperty(localName = "atpnQesitm")
        private String atpnQesitm;

        @JacksonXmlProperty(localName = "intrcQesitm")
        private String intrcQesitm;

        @JacksonXmlProperty(localName = "seQesitm")
        private String seQesitm;

        @JacksonXmlProperty(localName = "depositMethodQesitm")
        private String depositMethodQesitm;

        @JacksonXmlProperty(localName = "openDe")
        private String openDe;

        @JacksonXmlProperty(localName = "updateDe")
        private String updateDe;

        @JacksonXmlProperty(localName = "itemImage")
        private String itemImage;

        @JacksonXmlProperty(localName = "bizrno")
        private String bizrno;
    }
}
