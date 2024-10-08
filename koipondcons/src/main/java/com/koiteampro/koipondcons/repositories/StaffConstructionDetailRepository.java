package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffConstructionDetailRepository extends JpaRepository<StaffConstructionDetail, Long> {
    List<StaffConstructionDetail> findAllByConstructionOrderId(Long id);
}
