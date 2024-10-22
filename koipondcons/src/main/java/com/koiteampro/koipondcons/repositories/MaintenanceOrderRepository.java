package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
    MaintenanceOrder findByConstructorAccountIdAndStatusLike(long constructorAccount_id, String status);
//    List<MaintenanceOrder> findAllByConstructorAccountIdAndStatusNotLike(MaintenanceOrderStatus status);
}
