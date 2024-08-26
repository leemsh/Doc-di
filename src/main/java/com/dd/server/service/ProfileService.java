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


    public Profile getProfile(String email){
        logger.info("Received request with parameters: email={}", email);
        return profileRepository.findByEmail(email);
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

        profileRepository.save(profile);

        Profile newProfile = profileRepository.findByEmail(profileDto.getEmail());
        if(newProfile.getImage() == profileDto.getImage()) return new SuccessResponse<Profile>(true, newProfile);
        else return new SuccessResponse<>(false, null);
    }

    public boolean deleteProfile(String email){
        logger.info("Received request with parameters: email={}", email);

        Profile profile = profileRepository.findByEmail(email);

        logger.info("Deleting file: {}", profile.getImage());

        File file = new File(profile.getImage());
        boolean isDeleted = file.delete();
        if (!isDeleted) {
            logger.error("Failed to delete file {}", profile.getImage());
            throw new RuntimeException("Failed to delete file " + profile.getImage());
        } else {
            logger.info("File deleted successfully: {}", profile.getImage());
        }

        profileRepository.deleteByEmail(email);
        if(profileRepository.findByEmail(email) == null) return true;
        else return false;
    }
}
