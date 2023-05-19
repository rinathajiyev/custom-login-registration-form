package com.darkrinta.config;

import com.twilio.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class TwilioInitializer {

    private final TwilioConfiguration twilioConfiguration;
    private final static Logger LOGGER  = LoggerFactory.getLogger(TwilioInitializer.class);

    @Autowired
    public TwilioInitializer(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        Twilio.init(
                twilioConfiguration.getAccountSid(),
                twilioConfiguration.getAuthToken()
        );
        LOGGER.info("Twilio initialized with account id {}", twilioConfiguration.getAccountSid());
    }
}
