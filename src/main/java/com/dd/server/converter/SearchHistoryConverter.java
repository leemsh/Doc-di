package com.dd.server.converter;

import com.dd.server.domain.SearchHistory;
import com.dd.server.dto.SearchHistoryDto;

public class SearchHistoryConverter {
    public static SearchHistory toEntity(SearchHistoryDto searchHistoryDto){
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setEmail(searchHistoryDto.getEmail());
        searchHistory.setItemSeq(searchHistoryDto.getItemSeq());
        searchHistory.setMedicineName(searchHistoryDto.getMedicineName());
        searchHistory.setSearchTime(searchHistoryDto.getSearchTime());
        return searchHistory;
    }
}
