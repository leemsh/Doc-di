package com.dd.server.service;

import com.dd.server.domain.User;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;

    public SuccessResponse<String> sendTemporaryPassword(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[Doc-di] Your Temporary Password");
        message.setText("Your temporary password is: " + password);
        try {
            mailSender.send(message);
            logger.info("send email success");
            return new SuccessResponse<> ("send email Successfully", 200);
        }catch (Exception e) {
            return new SuccessResponse<> ("send email Failed", 500);
        }
    }

    public SuccessResponse<String> sendEmailConfirmCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[Doc-di] Email Confirm Code");
        message.setText("Your temporary password is: " + code);
        try {
            mailSender.send(message);
            logger.info("send email success");
            return new SuccessResponse<> ("send email Successfully", 200);
        }catch (Exception e) {
            return new SuccessResponse<> ("send email Failed", 500);
        }
    }

    public String makeRandomPassword() {
        int passwordLength = 6;
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(charSet.length());
            password.append(charSet.charAt(randomIndex));
        }

        return password.toString();
    }

    public void changePassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        try {
            userRepository.save(user);
            logger.info("change password success");
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}