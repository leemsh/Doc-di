package com.dd.server.service;

import com.dd.server.converter.StatisticsConverter;
import com.dd.server.domain.Statistics;
import com.dd.server.dto.StatisticsDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticRepository statisticRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse<List<Statistics>> getStatistics(String email){
        List<Statistics> data;
        try {
            data = statisticRepository.findByMedicineName(email);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("interner server error", 500);
        }
        if(data.isEmpty()){
            return new SuccessResponse("empty", 204);
        }
        return new SuccessResponse<>(data, 200);
    }


    public SuccessResponse<String> createStatistics(StatisticsDto statisticsDto){
        Statistics statistics = StatisticsConverter.toEntity(statisticsDto);
        try {
            statisticRepository.save(statistics);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse<>("DB save fail", 500);
        }
        return new SuccessResponse<>("DB save Success", 201);
    }
}
