package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
}
