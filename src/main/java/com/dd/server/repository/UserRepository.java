package com.dd.server.repository;

import com.dd.server.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Users> findByPhoneNum(String phoneNum);
    boolean existsByPhoneNum(String phoneNum);
    Optional<Users> findByRefreshToken(String refreshToken);
}
