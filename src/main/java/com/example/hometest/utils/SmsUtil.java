package com.example.hometest.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsUtil {

    public void sendSms(String phoneNumber, String message) {
        log.info("implement send sms");
    }
}
