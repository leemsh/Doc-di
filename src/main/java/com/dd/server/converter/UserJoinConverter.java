package com.dd.server.converter;

import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import com.dd.server.dto.MedicineResponse;

public class UserJoinConverter {
    public static User toEntity(JoinDto joinDto) {
        User user = new User();

        user.setId(joinDto.getId());
        user.setEmail(joinDto.getPassword());
        user.setSex(joinDto.getSex());
        user.setBirthday(joinDto.getBirthday());
        user.setHeight(joinDto.getHeight());
        user.setWeight(joinDto.getWeight());
        user.setBloodType(joinDto.getBloodType());
        user.setPhoneNum(joinDto.getPhoneNum());

        return user;
    }
}
