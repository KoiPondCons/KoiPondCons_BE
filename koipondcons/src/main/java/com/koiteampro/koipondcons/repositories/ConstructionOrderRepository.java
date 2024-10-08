package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstructionOrderRepository extends JpaRepository<ConstructionOrder, Long> {
    List<ConstructionOrder> findAllByStatusIs(ConstructionOrderStatus status);
    List<ConstructionOrder> findAllByCustomerId(Long customerId);
}
