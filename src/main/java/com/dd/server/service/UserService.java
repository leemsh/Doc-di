package com.dd.server.service;

import com.dd.server.domain.User;
import com.dd.server.dto.UserDto;
import com.dd.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReminderService reminderService;
    private final ProfileService profileService;

    public User getUser(String email){

        logger.info("Received request with parameters: email={}", email);

        return userRepository.findByEmail(email);
    }

    public User editUser(UserDto userDto, MultipartFile file){
        User user = userRepository.findByEmail(userDto.getEmail());

        String fileName = userDto.getImage();
        String imageRoute;
        imageRoute = profileService.resetImage(userDto.getEmail(), fileName, file);
        if (user == null){
            logger.error("User not found");
            throw new IllegalArgumentException("User not found with email: " + userDto.getEmail());
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setImage(imageRoute);
        try {
            userRepository.save(user);
            logger.info("User successfully updated");
        }catch (Exception e){
            logger.error("Error saving user", e);
            throw new IllegalArgumentException("Error saving user");
        }
        return user;
    }

    public boolean deleteUser(String email){
        reminderService.deleteReminderByEmail(email);
        userRepository.deleteByEmail(email);
        if(userRepository.findByEmail(email) == null) return true;
        return false;
    }

}
