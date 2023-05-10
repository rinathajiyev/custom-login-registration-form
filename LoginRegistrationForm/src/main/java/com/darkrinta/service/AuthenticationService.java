package com.darkrinta.service;

import com.darkrinta.dto.*;
import com.darkrinta.entity.User;
import com.darkrinta.repository.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public void authenticate(AuthenticationRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}
