package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.models.response.EmailDetail;
import com.koiteampro.koipondcons.models.response.EmailPaymentDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getEmail());
            context.setVariable("button", "Xác minh tài khoản");
            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("welcome-template", context);

            //Create a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            //setting up necessary details
            mimeMessageHelper.setFrom("trankimnha272727@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());

            //send mail
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error send email!");
        }
    }

    @Async
    public void sendFirstPaymentEmail(EmailPaymentDetail emailPaymentDetail) {
        try {
            Context context = new Context();
            context.setVariable("name", emailPaymentDetail.getReceiver().getEmail());
            context.setVariable("text1", emailPaymentDetail.getText1());
            context.setVariable("text2", emailPaymentDetail.getText2());
            context.setVariable("text3", emailPaymentDetail.getText3());
            context.setVariable("text4", emailPaymentDetail.getText4());
            context.setVariable("text5", emailPaymentDetail.getText5());
//            context.setVariable("button", "Xác minh tài khoản");
//            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("payment-notify", context);

            //Create a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            //setting up necessary details
            mimeMessageHelper.setFrom("trankimnha272727@gmail.com");
            mimeMessageHelper.setTo(emailPaymentDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailPaymentDetail.getSubject());

            //send mail
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error send email!");
        }
    }
}
