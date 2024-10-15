package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.repositories.ConsOrderPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConsOrderPaymentService {

    @Autowired
    private ConsOrderPaymentRepository consOrderPaymentRepository;

    public void addConsOrderPayment(ConstructionOrder constructionOrder) {
        if (constructionOrder.isDesigned()) {
            for (int i = 1; i <= 2; i++) {
                ConsOrderPayment consOrderPayment = new ConsOrderPayment();
                consOrderPayment.setConstructionOrder(constructionOrder);
                consOrderPayment.setPeriod(i);
                consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().divide(new BigDecimal(2)));
                consOrderPayment.setContent("Thanh toán đợt " + i);
                consOrderPaymentRepository.save(consOrderPayment);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ConsOrderPayment consOrderPayment = new ConsOrderPayment();
                consOrderPayment.setConstructionOrder(constructionOrder);
                consOrderPayment.setPeriod(i);
                consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().divide(new BigDecimal(3)));
                consOrderPayment.setContent("Thanh toán đợt " + i);
                consOrderPaymentRepository.save(consOrderPayment);
            }
        }
    }

}
