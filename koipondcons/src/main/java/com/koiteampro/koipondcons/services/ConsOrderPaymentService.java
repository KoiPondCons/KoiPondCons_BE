package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.enums.PaymentMethod;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.repositories.ConsOrderPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

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
                consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().multiply(new BigDecimal("0.5")));
                consOrderPayment.setContent("Thanh toán đợt " + i);
                consOrderPaymentRepository.save(consOrderPayment);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ConsOrderPayment consOrderPayment = new ConsOrderPayment();
                consOrderPayment.setConstructionOrder(constructionOrder);
                consOrderPayment.setPeriod(i);
                switch (i) {
                    case 1:
                        consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().multiply(new BigDecimal("0.2")));
                        break;
                    case 2:
                        consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().multiply(new BigDecimal("0.3")));
                        break;
                    case 3:
                        consOrderPayment.setAmount(constructionOrder.getQuotation().getFinalPrice().multiply(new BigDecimal("0.5")));
                        break;
                }
                consOrderPayment.setContent("Thanh toán đợt " + i);
                consOrderPaymentRepository.save(consOrderPayment);
            }
        }
    }
    public ConsOrderPayment updateConsOrderPayment(long consOrderPaymentId,int period, boolean isPaid, PaymentMethod paymentMethod) {
        Optional<ConsOrderPayment> consOrderPayment = consOrderPaymentRepository.findByConstructionOrderIdAndPeriod(consOrderPaymentId, period);

        if(consOrderPayment.isPresent()) {
            ConsOrderPayment consOrderPay = consOrderPayment.get();
            consOrderPay.setPaid(isPaid);
            consOrderPay.setPaidAt(LocalDateTime.now());
            consOrderPay.setPaymentMethod(paymentMethod);
            consOrderPaymentRepository.save(consOrderPay);
            return consOrderPay;
        }else{
            throw new NotFoundException("deo tim thay");
        }
    }

}
