package com.dd.server.repository;

import com.dd.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByPhoneNum(String phoneNum);
    boolean existsByPhoneNum(String phoneNum);
    User findByRefreshToken(String refreshToken);
}
