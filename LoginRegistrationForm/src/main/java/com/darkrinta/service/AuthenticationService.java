package com.darkrinta.service;

import com.darkrinta.dto.*;
import com.darkrinta.repository.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public void authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    public void authenticate(PhoneRequest request){
        var user = userRepository.findByPhone(request.getPhone()).orElseThrow();
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
    }

}
