package com.dd.server.service;

import com.dd.server.domain.Profile;
import com.dd.server.dto.ProfileDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public SuccessResponse<Profile> getProfile(String email){
        logger.info("Received request with parameters: email={}", email);
        Profile data;
        try {
            data = profileRepository.findByEmail(email);
        }catch(Exception e){
            return new SuccessResponse<>(null, 500);
        }
        return new SuccessResponse<>(data, 200);
    }

    public SuccessResponse<Profile> editProfile(ProfileDto profileDto){
        Profile profile = profileRepository.findByEmail(profileDto.getEmail());

        if (profile == null){
            logger.error("User not found");
            throw new IllegalArgumentException("User not found with email: " + profileDto.getEmail());
        }



        profile.setEmail(profileDto.getEmail());
        profile.setName(profileDto.getName());
        profile.setId(profile.getId());
        profile.setImage(profileDto.getImage());

        try{
            profileRepository.save(profile);
        }catch(Exception e){
            return new SuccessResponse<>(null, 500);
        }

        Profile newProfile = profileRepository.findByEmail(profileDto.getEmail());
        if(newProfile.getImage() == profileDto.getImage()) return new SuccessResponse<>(newProfile, 500);
        else return new SuccessResponse<>(null, 500);
    }

    public SuccessResponse<String> deleteProfile(String email){
        logger.info("Received request with parameters: email={}", email);

        Profile profile = profileRepository.findByEmail(email);

        logger.info("Deleting file: {}", profile.getImage());

        File file = new File(profile.getImage());
        boolean isDeleted = file.delete();
        if (!isDeleted) {
            logger.error("Failed to delete file {}", profile.getImage());
            return new SuccessResponse<>("Failed to delete file "+profile.getImage(), 500);
        } else {
            logger.info("File deleted successfully: {}", profile.getImage());
        }

        profileRepository.deleteByEmail(email);
        if(profileRepository.findByEmail(email) == null) return new SuccessResponse<>("Success to delete file "+profile.getImage(), 200);
        else return new SuccessResponse<>("Failed to delete file "+profile.getImage(), 500);
    }
}
