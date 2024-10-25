package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsOrderPaymentRepository extends JpaRepository<ConsOrderPayment, Long> {
    Optional<ConsOrderPayment> findByConstructionOrderIdAndPeriod(Long orderId, int period);
}
