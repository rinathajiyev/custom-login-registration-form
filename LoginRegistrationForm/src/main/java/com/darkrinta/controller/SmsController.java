package com.darkrinta.controller;

import com.darkrinta.dto.*;
import com.darkrinta.entity.*;
import com.darkrinta.repository.*;
import com.darkrinta.service.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import java.util.*;

@Controller
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final UserRepository userRepository;
    private final TwilioService twilioService;

    @GetMapping("/forgetPassword")
    public ModelAndView forgotPassword(){
        ModelAndView mv = new ModelAndView("forget-password");
        mv.addObject("phoneRequest", new PhoneRequest());
        return mv;
    }

    @PostMapping("/smsCode")
    public ModelAndView smsCode(@ModelAttribute("phoneNumber") PhoneRequest phoneRequest){
        Optional<User> user = userRepository.findByPhone(phoneRequest.getPhone());
        if(user.isEmpty() || !user.get().isEnabled()){
            System.out.println(new IllegalArgumentException("Phone Number did not found!"));
            ModelAndView mv = new ModelAndView("forget-password");
            return mv;
        } else{
            twilioService.sendSms(phoneRequest);
            ModelAndView mv = new ModelAndView("sms-code");
            return mv;
        }
    }

    @PostMapping("/newPassword")
    public ModelAndView newPassword(){
        ModelAndView mv = new ModelAndView("new-password");
        return mv;
    }
}
