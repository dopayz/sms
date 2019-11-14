package com.yudu.sms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsConfig {
	@Value("${isEnable}")
	public boolean isEnable;
	@Value("${useSmsMode}")
	public boolean useSmsMode;
	@Value("${useSmsInterface}")
	public boolean useSmsInterface;
	@Value("${portName}")
	public String portName ;
	@Value("${isSmsCat}")
	public String isSmsCat ;
	@Value("${isEMA}")
	public String isEMA ;
}
