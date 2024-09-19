package com.dd.server.service;

import com.dd.server.domain.CheckCode;
import com.dd.server.domain.User;
import com.dd.server.dto.CheckCodeDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.dto.UserDto;
import com.dd.server.repository.CheckCodeRepository;
import com.dd.server.repository.RefreshRepository;
import com.dd.server.repository.StatisticRepository;
import com.dd.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReminderService reminderService;
    private final ProfileService profileService;
    private final RefreshRepository refreshRepository;
    private final StatisticsService statisticsService;
    private final MailService mailService;
    private final CheckCodeRepository checkCodeRepository;


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

    @Transactional
    public boolean deleteUser(String email) {
        try {
            reminderService.deleteReminderByEmail(email);
            reminderService.deleteBookedReminderByEmail(email);
            profileService.deleteImage(email);
            userRepository.deleteByEmail(email);
            refreshRepository.deleteByUsername(email);
            statisticsService.deleteStatisticsByEmail(email);

            // 사용자가 삭제되었는지 확인
            return userRepository.findByEmail(email) == null;
        } catch (Exception e) {
            // 예외 발생 시 로그를 남기고 false를 반환
            logger.error("Error deleting user with email: " + email, e);
            return false;
        }
    }

    @Transactional
    public SuccessResponse<String> requestCode(String email) {
        CheckCode legacyCheckCode = null;

        try {
            legacyCheckCode = checkCodeRepository.findByEmail(email);
        } catch (Exception e) {
            logger.warn("Error checking code for email: " + email, e);
        }

        if(legacyCheckCode != null){
            checkCodeRepository.delete(legacyCheckCode);
        }


        CheckCode checkCode = new CheckCode();
        checkCode.setEmail(email);
        String code = mailService.makeRandomPassword();
        checkCode.setCode(code);
        try {
            checkCodeRepository.save(checkCode);
            mailService.sendEmailConfirmCode(email, code);
            return new SuccessResponse<>("send email successfully", 201);
        }catch (Exception e){
            logger.error("Error saving check code", e);
            return new SuccessResponse<>("error in making checkCode", 500);
        }
    }

    @Transactional
    public SuccessResponse<String> checkCode(CheckCodeDto checkCodeDto){
        CheckCode legacyCheckCode = null;

        try {
            legacyCheckCode = checkCodeRepository.findByEmail(checkCodeDto.getEmail());
        } catch (Exception e) {
            logger.warn("Error checking code for email: " + checkCodeDto.getEmail(), e);
        }

        boolean isSameCode = legacyCheckCode.getCode().equals(checkCodeDto.getCode());
        if(isSameCode) checkCodeRepository.delete(legacyCheckCode);
        if(legacyCheckCode.getEmail() != null){
            if(isSameCode){
                return new SuccessResponse<>("check code successfully", 200);
            }else{
                return new SuccessResponse<>("check code failed", 500);
            }
        }else{
            return new SuccessResponse<>("check code failed", 500);
        }
    }
}
