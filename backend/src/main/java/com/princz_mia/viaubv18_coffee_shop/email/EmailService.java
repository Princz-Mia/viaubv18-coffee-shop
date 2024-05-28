package com.princz_mia.viaubv18_coffee_shop.email;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.princz_mia.viaubv18_coffee_shop.email.EmailUtils.getEmailMessage;
import static com.princz_mia.viaubv18_coffee_shop.email.EmailUtils.getResetPasswordMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    private static final String PASSWORD_RESET_REQUEST = "Reset Password Request";
    private final JavaMailSender sender;

    @Value("http://localhost:3000")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendNewAccountEmail(String name, String toEmail, String key) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(getEmailMessage(name, host, key));
            sender.send(message);
        } catch (Exception e) {
            throw new AppException("Unable to send email, cause: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @Async
    public void sendPasswordResetEmail(String name, String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(getResetPasswordMessage(name, host, token));
            sender.send(message);
        } catch (Exception e) {
            throw new AppException("Unable to send email, cause: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
