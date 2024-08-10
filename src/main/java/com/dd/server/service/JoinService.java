package com.dd.server.service;

import com.dd.server.converter.UserJoinConverter;
import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import com.dd.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;

    public JoinService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean joinProcess(JoinDto joinDto){

        Boolean isExist = userRepository.existsByEmail(joinDto.getEmail());

        if(isExist){ // 회원정보가 이미 있음
            return false;
        }

        User user = UserJoinConverter.toEntity(joinDto);
        userRepository.save(user);

        return true;
    }
}
