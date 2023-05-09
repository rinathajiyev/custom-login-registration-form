package com.darkrinta.controller;

import com.darkrinta.dto.*;
import com.darkrinta.service.*;
import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.mail.*;
import javax.servlet.http.*;
import java.io.*;

@Controller
@RequestMapping("/form")
@RequiredArgsConstructor
public class FormController {

    private final RegisterService registerService;
    private final JwtService jwtService;

    @GetMapping("/login-register-page")
    public ModelAndView loginRegisterPage(){
        ModelAndView mv = new ModelAndView("login-register-page");
        mv.addObject("registerRequest", new RegisterRequest());
        return mv;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute RegisterRequest request,
                                 HttpServletResponse httpResponse,
                                 HttpServletRequest httpRequest)
            throws MessagingException, UnsupportedEncodingException {
        UserDetails user = registerService.generateUser(request, getSiteUrl(httpRequest));
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        ModelAndView mv = new ModelAndView("email-verification");
        mv.addObject("username", username);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);

        return mv;
    }

    @GetMapping("/verify")
    public ModelAndView verifyAccount (@RequestParam String code, HttpServletRequest request){
        if(registerService.verify(code)){
            String token = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie:cookies){
                    if(cookie.getName().equals("jwt")){
                        token = cookie.getValue();
                    }
                }
            }
            String username = jwtService.extractUsername(token);
            ModelAndView mv = new ModelAndView("success");
            mv.addObject("username", username);
            return mv;
        } else{
            ModelAndView mv = new ModelAndView("fail");
            return mv;
        }
    }

    private String getSiteUrl(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        url = url.replace(request.getServletPath(), "");

        return url;
    }
}
