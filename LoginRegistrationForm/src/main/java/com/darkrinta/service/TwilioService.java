package com.darkrinta.service;

import com.darkrinta.config.*;
import com.darkrinta.dto.*;
import com.darkrinta.entity.*;
import com.darkrinta.repository.*;
import com.twilio.rest.api.v2010.account.*;
import lombok.*;
import org.springframework.stereotype.*;
import com.twilio.type.PhoneNumber;

import java.text.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TwilioService {

    private final TwilioConfiguration twilioConfiguration;
    private final UserRepository userRepository;

    public void sendSms(PhoneRequest phoneRequest){
        String otp = generateOtp();

        Optional<User> user = userRepository.findByPhone(phoneRequest.getPhone());
        user.get().setSmsCode(otp);
        userRepository.save(user.get());

        PhoneNumber to = new PhoneNumber(phoneRequest.getPhone());
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
        String message = "Your verification code: " + otp;
        MessageCreator creator = Message.creator(to, from, message);
        creator.create();
    }

    private String generateOtp(){
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
