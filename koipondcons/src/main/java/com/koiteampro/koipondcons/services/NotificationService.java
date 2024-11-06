package com.koiteampro.koipondcons.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.models.request.NotificationFCM;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public FirebaseMessaging firebaseMessaging;

    public NotificationService(FirebaseApp firebaseApp) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    public void sendNotification(NotificationFCM notificationFCM) {
        Notification notification = Notification.builder()
                .setTitle(notificationFCM.getTitle())
                .setBody(notificationFCM.getMessage())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(notificationFCM.getFcmToken())
                .build();

        try{
            firebaseMessaging.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendNotificationToAccount(NotificationFCM notificationFCM, Account account) {
        Notification notification = Notification.builder()
                .setTitle(notificationFCM.getTitle())
                .setBody(notificationFCM.getMessage())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(account.getFcmToken())
                .build();

        try{
            firebaseMessaging.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
