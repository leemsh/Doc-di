package com.dd.server.repository;

import com.dd.server.domain.CheckCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckCodeRepository extends JpaRepository<CheckCode, Integer> {
    CheckCode findByEmail(String email);
    void deleteByEmail(String email);
}
