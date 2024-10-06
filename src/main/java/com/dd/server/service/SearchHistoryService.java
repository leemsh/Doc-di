package com.dd.server.service;

import com.dd.server.converter.SearchHistoryConverter;
import com.dd.server.domain.SearchHistory;
import com.dd.server.dto.SearchHistoryDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.SearchHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuccessResponse<List<SearchHistory>> getHistory(String email){
        List<SearchHistory> data;
        try{
            data = searchHistoryRepository.findByEmail(email);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("db error", 500);
        }
        if(data.isEmpty()) return new SuccessResponse("empty", 204);
        return new SuccessResponse<>(data, 200);
    }

    public void createHistory(SearchHistoryDto searchHistoryDto){
        SearchHistory searchHistory = SearchHistoryConverter.toEntity(searchHistoryDto);
        try{
            searchHistoryRepository.save(searchHistory);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    @Transactional
    public SuccessResponse<String> deleteHistory(int id){
        try{
            searchHistoryRepository.deleteById(id);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new SuccessResponse("DB delete fail", 500);
        }
        return new SuccessResponse("DB delete success", 200);
    }
}
