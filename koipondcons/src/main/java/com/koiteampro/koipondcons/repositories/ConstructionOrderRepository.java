package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstructionOrderRepository extends JpaRepository<ConstructionOrder, Long> {
    List<ConstructionOrder> findAllByStatusIs(ConstructionOrderStatus status);
    List<ConstructionOrder> findAllByCustomerId(Long customerId);
    List<ConstructionOrder> findAllByConsultantAccountId(Long consultantAccountId);

    @Query("SELECT scd.constructionOrder " +
            "from StaffConstructionDetail scd " +
            "where scd.constructorAccount.id = :ConstructorId " +
            "group by scd.constructionOrder " +
            "having count(scd) = sum(case when scd.isFinished = true then 1 else 0 end )")
    List<ConstructionOrder> findFinishedOrdersByConstructorID(@Param("ConstructorId") Long constructorId);
}
