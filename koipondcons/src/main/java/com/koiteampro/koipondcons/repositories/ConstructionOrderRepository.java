package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstructionOrderRepository extends JpaRepository<ConstructionOrder, Long> {
    List<ConstructionOrder> findAllByStatusIs(ConstructionOrderStatus status);
    List<ConstructionOrder> findAllByCustomerIdAndStatusNot(Long customerId, ConstructionOrderStatus status);
    List<ConstructionOrder> findAllByConsultantAccountId(Long consultantAccountId);
    List<ConstructionOrder> findAllByConsultantAccountIdAndStatusNot(Long consultantAccountId, ConstructionOrderStatus status);
    List<ConstructionOrder> findAllByConsultantAccountIdAndStatusIs(Long consultantAccountId, ConstructionOrderStatus status);
    long countByStatus(ConstructionOrderStatus status);

    @Query("SELECT scd.constructionOrder " +
            "from StaffConstructionDetail scd " +
            "where scd.constructorAccount.id = :ConstructorId " +
            "group by scd.constructionOrder " +
            "having count(scd) = sum(case when scd.isFinished = true then 1 else 0 end )")
    List<ConstructionOrder> findFinishedOrdersByConstructorID(@Param("ConstructorId") Long constructorId);

    @Query(value =
            "SELECT COUNT(CombinedOrder.customer_id) AS customer_count\n" +
                    "FROM (\n" +
                    "    SELECT c.customer_id FROM construction_order c WHERE c.status = 'CLOSED'\n" +
                    "    UNION\n" +
                    "    SELECT m.customer_id FROM maintenance_order m WHERE m.status = 'FINISHED'\n" +
                    ") CombinedOrder",
            nativeQuery = true
    )
    long countCustomersUsedService();
}
