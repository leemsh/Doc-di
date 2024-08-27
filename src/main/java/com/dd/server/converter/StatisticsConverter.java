package com.dd.server.converter;

import com.dd.server.domain.Statistics;
import com.dd.server.dto.StatisticsDto;

public class StatisticsConverter {
    public static Statistics toEntity(StatisticsDto statisticsDto){
        Statistics statistics = new Statistics();
        statistics.setName(statisticsDto.getName());
        statistics.setEmail(statisticsDto.getEmail());
        statistics.setStatistic(statisticsDto.getStatistic());
        statistics.setMedicineName(statisticsDto.getMedicineName());
        return statistics;
    }
}
