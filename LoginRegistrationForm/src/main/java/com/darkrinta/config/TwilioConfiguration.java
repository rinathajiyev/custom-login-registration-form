package com.darkrinta.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwilioConfiguration {
    private String accountSid;
    private String authToken;
    private String trialNumber;
}
