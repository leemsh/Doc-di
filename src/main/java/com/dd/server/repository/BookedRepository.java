package com.dd.server.repository;

import com.dd.server.domain.Booked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookedRepository extends JpaRepository<Booked, Integer> {
    List<Booked> findByEmail(String email);
    Booked findById(int id);
    void deleteById(int id);
    void deleteByEmail(String email);
}
