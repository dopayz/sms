package com.yudu.sms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EMAConfig {
    @Value("${EMAuserName}")
    public String EMAuserName;
    @Value("${EMApassword}")
    public String EMApassword;
}
