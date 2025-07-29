package com.example.hometest.serviceImpl.notification;

import com.example.hometest.model.entity.user.User;
import com.example.hometest.repo.user.UserRepository;
import com.example.hometest.service.notification.NotificationService;
import com.example.hometest.utils.EmailUtil;
import com.example.hometest.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendNotification(Long userId, String content) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(()->new Exception("Invalid User"));
        if (user.isEmailEnabled()){
            if (user.getEmail() == null || user.getEmail().isEmpty()){
                throw new Exception("Invalid Email");
            }
            emailUtil.sendEmail(user.getEmail(),content);
        }
        if (user.isSmsEnabled()){
            if (user.getPhone()== null || user.getPhone().isEmpty()){
                throw new Exception("Invalid Phone");
            }
            smsUtil.sendSms(user.getPhone(),content);
        }
    }

}
