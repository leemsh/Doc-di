package com.dd.server.service;

import com.dd.server.domain.User;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.dto.UserDto;
import com.dd.server.repository.ProfileRepository;
import com.dd.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileRepository profileRepository;

    public User getUser(String email){

        logger.info("Received request with parameters: email={}", email);

        return userRepository.findByEmail(email);
    }

    public User editUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.getEmail());

        if (user == null){
            logger.error("User not found");
            throw new IllegalArgumentException("User not found with email: " + userDto.getEmail());
        }
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getEmail());
        user.setName(userDto.getName());

        userRepository.save(user);

        return userRepository.findByEmail(user.getEmail());
    }

    public boolean deleteUser(String email){
        userRepository.deleteByEmail(email);
        profileRepository.deleteByEmail(email);
        if(userRepository.findByEmail(email) == null) return true;
        return false;
    }

}
