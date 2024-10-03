package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Account;
import lombok.Data;

@Data
public class EmailDetail {
    Account receiver;
    String subject;
    String link;
}
