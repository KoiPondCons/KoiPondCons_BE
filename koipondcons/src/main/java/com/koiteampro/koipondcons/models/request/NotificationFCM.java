package com.koiteampro.koipondcons.models.request;

import lombok.Data;

@Data
public class NotificationFCM {
    String title;
    String message;
    String fcmToken;
}
