package com.dd.server.controller;

import com.dd.server.dto.MedicineInfoResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class MedicineInfoApiController {

    private static final String REQUEST_HOST = "apis.data.go.kr";
    private static final String REQUEST_PATH = "/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList";
    private static final String KEY = "%2FqfclHAZhBhEgnRIT3tgBVKrwqZc6EheBLisxgGFp%2FsTsd1TcivMatomLb1mn1ANjulMpmck74G3BiYhd6pIyg%3D%3D";

    private final WebClient webClient;
    private final XmlMapper xmlMapper;
    private static final Logger logger = LoggerFactory.getLogger(MedicineInfoApiController.class);

    public MedicineInfoApiController(WebClient webClient, XmlMapper xmlMapper){
        this.webClient = webClient;
        this.xmlMapper = xmlMapper;
        this.xmlMapper.registerModule(new JaxbAnnotationModule());
    }

    public Mono<MedicineInfoResponse> getMedicineInfoFormApi(String itemSeq){
        String encodedName = null;

        try{
            if(itemSeq != null){
                encodedName = URLEncoder.encode(itemSeq, StandardCharsets.UTF_8.toString());
            }
        } catch (Exception e){
            logger.error("Failed to encode parameter", e);
            return Mono.error(new RuntimeException("railed to encode parameter", e));
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("HTTP")
                .host(REQUEST_HOST)
                .path(REQUEST_PATH)
                .queryParam("serviceKey", KEY);

        uriBuilder.queryParam("itemSeq", encodedName);


        String uri = uriBuilder.build().toUriString();

        logger.info("Request URL: {}", uri);

        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Response Body: {}", response))
                .map(this::convertXmlToMedicineInfoResponse);
    }


    private MedicineInfoResponse convertXmlToMedicineInfoResponse(String xml) {
        try {
            logger.info("Converting XML to MedicineInfoResponse");
            MedicineInfoResponse response = xmlMapper.readValue(xml, MedicineInfoResponse.class);
            // logger.info("Converted MedicineInfoResponse: " + response.getBody().getItems().get(0).getItemName());
            return response;
        } catch (Exception e) {
            logger.error("Failed to parse XML to MedicineResponse", e);
            return new MedicineInfoResponse();
        }
    }
}
