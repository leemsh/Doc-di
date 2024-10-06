package com.dd.server.repository;

import com.dd.server.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {
    List<SearchHistory> findByEmail(String email);
    void deleteById(int id);
}
