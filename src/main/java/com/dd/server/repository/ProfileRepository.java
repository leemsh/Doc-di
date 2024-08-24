package com.dd.server.repository;

import com.dd.server.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByEmail(String email);
    void deleteByEmail(String email);
}
