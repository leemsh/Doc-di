package com.dd.server.service;

import com.dd.server.converter.UserJoinConverter;
import com.dd.server.domain.User;
import com.dd.server.dto.JoinDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final ProfileService profileService;

    public SuccessResponse<JoinDto> joinProcess(JoinDto joinDto, MultipartFile imageFile){

        Boolean isExist = userRepository.existsByEmail(joinDto.getEmail());
        if(isExist){
            return new SuccessResponse("Already exist email : "+joinDto.getEmail(), 409); // 회원정보가 이미 있음
        }

        String fileName = joinDto.getImage();
        String imageRoute;

        try {
            imageRoute = profileService.createImage(fileName, imageFile);
        }catch(Exception e){
            e.printStackTrace();
            return new SuccessResponse("Image creation failed in S3", 409);
        }

        User user = UserJoinConverter.toEntity(joinDto);
        user.setImage(imageRoute);

        try{
            userRepository.save(user);
        } catch (Exception e){
            return new SuccessResponse("fail to save in DB", 500); //db 저장 실패
        }
        return new SuccessResponse<>(joinDto, 201); //회원가입 성공
    }
}
