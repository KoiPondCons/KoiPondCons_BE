package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Account;
import lombok.Data;

@Data
public class EmailPaymentDetail {
    Account receiver;
    String subject;
    String text1;
    String text2;
    String text3;
    String text4;
    String text5;
}
