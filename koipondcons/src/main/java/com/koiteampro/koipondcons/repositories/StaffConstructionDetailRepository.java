package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StaffConstructionDetailRepository extends JpaRepository<StaffConstructionDetail, Long> {
    List<StaffConstructionDetail> findAllByConstructionOrderId(Long id);

    @Query(
            "select d.constructorAccount.id\n" +
            "from StaffConstructionDetail d\n" +
            "where d.isFinished = false"
    )
    List<Long> findStaffIdsWithUnfinishedWorks();

    List<StaffConstructionDetail> findByIsFinishedFalseAndConstructorAccountId(Long id);
    long countByConstructionOrderId(Long id);
    long countByConstructionOrderIdAndIsFinishedTrue(Long id);
}
