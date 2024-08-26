package com.dd.server.service;

import com.dd.server.converter.ProfileConverter;
import com.dd.server.converter.UserJoinConverter;
import com.dd.server.domain.Profile;
import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import com.dd.server.dto.SuccessResponse;
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

    public SuccessResponse<JoinDto> joinProcess(JoinDto joinDto){

        Boolean isExist = userRepository.existsByEmail(joinDto.getEmail());

        if(isExist){
            return new SuccessResponse("Already exist email : "+joinDto.getEmail(), 409); // 회원정보가 이미 있음
        }

        isExist = userRepository.existsByPhoneNum(joinDto.getPhoneNum());
        if(isExist){
            return new SuccessResponse("Already exist phoneNum : "+joinDto.getPhoneNum(), 409); // 회원정보가 이미 있음
        }

        User user = UserJoinConverter.toEntity(joinDto);
        Profile profile = ProfileConverter.JoinDtoToProfile(joinDto);
        try{
            userRepository.save(user);
            profileRepository.save(profile);
        } catch (Exception e){
            return new SuccessResponse("fail to save in DB", 500); //db 저장 실패
        }
        return new SuccessResponse<>(joinDto, 201); //회원가입 성공
    }
}
