package com.dd.server.service;

import com.dd.server.dto.FindByMedicineChartDto;
import com.dd.server.dto.MedicineResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MedicineApiService {

    private static final String REQUEST_HOST = "apis.data.go.kr";
    private static final String REQUEST_PATH = "/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01";
    private static final String KEY = "%2FqfclHAZhBhEgnRIT3tgBVKrwqZc6EheBLisxgGFp%2FsTsd1TcivMatomLb1mn1ANjulMpmck74G3BiYhd6pIyg%3D%3D";
    private static final String TYPE = "xml";

    private final WebClient webClient;
    private final XmlMapper xmlMapper; // Jackson XML Mapper 추가
    private static final Logger logger = LoggerFactory.getLogger(MedicineApiService.class);

    public MedicineApiService(WebClient webClient, XmlMapper xmlMapper) {
        this.webClient = webClient;
        this.xmlMapper = xmlMapper;
        this.xmlMapper.registerModule(new JaxbAnnotationModule());
    }

    public Mono<MedicineResponse> getMedicineFromApi(FindByMedicineChartDto findByMedicineChartDto) {
        String encodedColor1;
        try {
            encodedColor1 = URLEncoder.encode(findByMedicineChartDto.getColor1(), StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            logger.error("Failed to encode parameter", e);
            return Mono.error(new RuntimeException("Failed to encode parameter", e));
        }

        String uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(REQUEST_HOST)
                .path(REQUEST_PATH)
                .queryParam("serviceKey", KEY)
                .queryParam("type", TYPE)
                .queryParam("chart", encodedColor1)
                .build()
                .toUriString();

        logger.info("Request URL: {}", uri);

        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .retrieve()
                .bodyToMono(String.class) // XML 응답을 문자열로 받기
                .doOnNext(response -> logger.info("Response Body: {}", response)) // 응답 로그 찍기
                .map(this::convertXmlToMedicineResponse); // XML 문자열을 MedicineResponse로 변환
    }

    private MedicineResponse convertXmlToMedicineResponse(String xml) {
        try {
            logger.info("Converting XML to MedicineResponse");
            MedicineResponse response = xmlMapper.readValue(xml, MedicineResponse.class);
            logger.info("Converted MedicineResponse: " + response.getBody().getItems().get(0).getItemName());
            return response;
        } catch (Exception e) {
            logger.error("Failed to parse XML to MedicineResponse", e);
            throw new RuntimeException("Failed to parse XML to MedicineResponse", e);
        }
    }
}
