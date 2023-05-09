package com.darkrinta.config;

import com.darkrinta.service.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if(jwtService.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);

    }
}
