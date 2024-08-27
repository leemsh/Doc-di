package com.dd.server.converter;

import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserJoinConverter {
    public static User toEntity(JoinDto joinDto) {
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setEmail(joinDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        user.setName(joinDto.getName());
        return user;
    }
}
