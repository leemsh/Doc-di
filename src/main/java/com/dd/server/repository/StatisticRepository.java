package com.dd.server.repository;

import com.dd.server.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistics, String> {
    List<Statistics> findByMedicineName(String medicineName);
}
