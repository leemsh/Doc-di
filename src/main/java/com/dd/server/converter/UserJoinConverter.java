package com.dd.server.converter;

import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserJoinConverter {
    public static User toEntity(JoinDto joinDto) {
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setId(joinDto.getId());
        user.setEmail(joinDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        user.setName(joinDto.getName());
        user.setSex(joinDto.getSex());
        user.setBirthday(joinDto.getBirthday());
        user.setHeight(joinDto.getHeight());
        user.setWeight(joinDto.getWeight());
        user.setBloodType(joinDto.getBloodType());
        user.setPhoneNum(joinDto.getPhoneNum());
        user.setRole("ROLE_ADMIN");
        return user;
    }
}
