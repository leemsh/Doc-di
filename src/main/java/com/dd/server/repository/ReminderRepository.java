package com.dd.server.repository;

import com.dd.server.domain.Reminder;
import com.dd.server.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, String> {
    List<Reminder> findByEmail(String email);
}
