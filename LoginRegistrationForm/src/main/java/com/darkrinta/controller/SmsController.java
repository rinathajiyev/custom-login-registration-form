package com.darkrinta.controller;

import com.darkrinta.dto.*;
import com.darkrinta.entity.*;
import com.darkrinta.repository.*;
import com.darkrinta.service.*;
import lombok.*;
import org.springframework.security.crypto.password.*;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/forgetPassword")
    public ModelAndView forgotPassword(){
        ModelAndView mv = new ModelAndView("forget-password");
        mv.addObject("phoneRequest", new PhoneRequest());
        return mv;
    }

    @PostMapping("/smsCode")
    public ModelAndView smsCode(@ModelAttribute("phoneRequest") PhoneRequest phoneRequest){
        Optional<User> user = userRepository.findByPhone(phoneRequest.getPhone());
        if(user.isEmpty() || !user.get().isEnabled()){
            System.out.println(new IllegalArgumentException("Phone Number did not found!"));
            ModelAndView mv = new ModelAndView("forget-password");
            mv.addObject("phoneRequest", new PhoneRequest());
            return mv;
        } else{
            twilioService.sendSms(phoneRequest);
            ModelAndView mv = new ModelAndView("sms-code");
            mv.addObject("smsCode", new SmsRequest(phoneRequest.getPhone(), null));
            return mv;
        }
    }

    @PostMapping("/newPassword")
    public ModelAndView newPassword(@ModelAttribute("smsCode") SmsRequest smsRequest){
        Optional<User> user = userRepository.findBySmsCode(smsRequest.getSmsCode());

        if(!user.isEmpty()){
            user.get().setSmsCode(null);
            userRepository.save(user.get());
            ModelAndView mv = new ModelAndView("new-password");
            mv.addObject("resetPassword", new ResetPassword(smsRequest.getPhone(), null, null));
            return mv;
        } else{
            ModelAndView mv = new ModelAndView("sms-code");
            mv.addObject("smsCode", new SmsRequest());
            return mv;
        }
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("resetPassword") ResetPassword resetPassword){
        Optional<User> user = userRepository.findByPhone(resetPassword.getPhone());
        String password = resetPassword.getPassword();
        String confirmPassword = resetPassword.getConfirmPassword();

        if(password.equals(confirmPassword)){
            ModelAndView mv = new ModelAndView("password-changed");
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("new-password");
            return mv;
        }
    }
}
