package com.example.hometest.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailUtil {
    public void sendEmail(String email, String content){
        log.info("implement send email");
    }
}
