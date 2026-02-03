package com.Rothana.hotel_booking_system.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class FileConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dkloakzs6");
        config.put("api_key", "162361396439147");
        config.put("api_secret", "DVu7SNnY50pVCr7xxY1mRmsO4Os");
        config.put("secure", "true");


        return new Cloudinary(config);

    }
}
