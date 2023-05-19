package com.darkrinta.service;

import com.darkrinta.dto.*;
import com.darkrinta.entity.User;
import com.darkrinta.repository.*;
import lombok.*;
import net.bytebuddy.utility.*;
import org.springframework.mail.javamail.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JavaMailSender emailSender;

    public UserDetails generateUser(RegisterRequest request, String url)
            throws MessagingException, UnsupportedEncodingException {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        String randomString = RandomString.make(64);
        user.setVerificationCode(randomString);
        user.setEnabled(false);

        userRepository.save(user);
        sendVerificationEmail(user, url);

        return user;
    }

    private void sendVerificationEmail(User user, String url)
            throws MessagingException, UnsupportedEncodingException {
        final String toAddress = user.getEmail();
        final String fromAddress = "haciyev.rinat.2002@gmail.com";
        final String senderName = "Rinat";
        final String subject = "Please confirm your email!";
        String content = "Dear [[name]], <br>"
                + "Please click the link below to verify your registration: <br>"
                + "<h4><a href=\"[[url]]\" target=\"_blank\">click here to verify your email</a></h4>"
                + "Thank you! <br>"
                +  "Best regards, "+ senderName;

        content = content.replace("[[name]]", user.getUsername());
        String verifyUrl = url + "/form/verify?code=" + user.getVerificationCode();
        content = content.replace("[[url]]", verifyUrl);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        emailSender.send(message);

    }

    public boolean verify(String verificationCode){
        Optional<User> user = userRepository.findByVerificationCode(verificationCode);

        if(user.isEmpty() || user.get().isEnabled()){
            return false;
        } else{
            user.get().setVerificationCode(null);
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return true;
        }

    }
}
