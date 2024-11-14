package com.dd.server.dto;

import lombok.Data;

import java.util.List;
@Data

public class PillPredictReceiveDto {
    private List<String> color;
    private List<String> shape;
    private List<List<String>> similar_color;
}
