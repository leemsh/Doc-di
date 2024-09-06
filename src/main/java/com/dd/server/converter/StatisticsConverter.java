package com.dd.server.converter;

import com.dd.server.domain.Statistics;
import com.dd.server.dto.StatisticsDto;

public class StatisticsConverter {
    public static Statistics toEntity(StatisticsDto statisticsDto){
        Statistics statistics = new Statistics();
        statistics.setName(statisticsDto.getName());
        statistics.setEmail(statisticsDto.getEmail());
        statistics.setStatistic(statisticsDto.getStatistic());
        statistics.setDate(statisticsDto.getDate());
        statistics.setMedicineName(statisticsDto.getMedicineName());
        statistics.setRate(statisticsDto.getRate());
        return statistics;
    }
}
