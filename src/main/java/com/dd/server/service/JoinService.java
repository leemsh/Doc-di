package com.dd.server.service;

import com.dd.server.converter.ProfileConverter;
import com.dd.server.converter.UserJoinConverter;
import com.dd.server.domain.Profile;
import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import com.dd.server.repository.ProfileRepository;
import com.dd.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public JoinService(UserRepository userRepository, ProfileRepository profileRepository){
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public boolean joinProcess(JoinDto joinDto){

        Boolean isExist = userRepository.existsByEmail(joinDto.getEmail());

        if(isExist){ // 회원정보가 이미 있음
            return false;
        }

        User user = UserJoinConverter.toEntity(joinDto);
        Profile profile = ProfileConverter.JoinDtoToProfile(joinDto);

        userRepository.save(user);
        profileRepository.save(profile);

        return true;
    }
}
