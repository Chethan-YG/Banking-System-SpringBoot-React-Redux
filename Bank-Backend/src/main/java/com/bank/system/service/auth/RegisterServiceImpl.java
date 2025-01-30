package com.bank.system.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.system.entity.User;
import com.bank.system.helpers.HTML;
import com.bank.system.helpers.Token;
import com.bank.system.repository.UserRepository;
import com.bank.system.utils.MailMessenger;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final MailMessenger mailMessenger;
    
    @Value("${spring.mail.username}")
    private String mailUsername;

    @Transactional
    public String registerUser(User user, String confirmPassword) {
         if (!user.getPassword().equals(confirmPassword)) {
            return "Passwords do not match";
        }

         Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "User with this email already exists.";
        }

         String token = Token.generateToken();
         int verificationCode = new Random().nextInt(1000000);  
         String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
         userRepository.registerUser(user.getFirstname(), user.getLastname(), user.getEmail(), hashedPassword, token, Integer.toString(verificationCode));

         String emailBody = HTML.htmlEmailTemplate(token, Integer.toString(verificationCode));

         sendNotification(user.getEmail(), "Verify Account", emailBody);

        return "Registration successful. Please check your email to verify your account.";
    }
    
    public void sendNotification(String email, String subject, String body) {
        try {
            mailMessenger.htmlEmailMessenger(mailUsername, email, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
