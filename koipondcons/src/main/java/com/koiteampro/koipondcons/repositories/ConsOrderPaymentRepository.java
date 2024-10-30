package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsOrderPaymentRepository extends JpaRepository<ConsOrderPayment, Long> {
    Optional<ConsOrderPayment> findByConstructionOrderIdAndPeriod(Long orderId, int period);

    @Query(value =
            "select year(combined.paid_at) as year, month(combined.paid_at) as month, sum(combined.amount) as monthlyRevenue\n" +
                    "from\n" +
                    "(select c.amount, c.paid_at\n" +
                    "from cons_order_payment c\n" +
                    "where c.paid_at is not null\n" +
                    "union\n" +
                    "select m.price as amount, m.end_date as paid_at\n" +
                    "from maintenance_order m\n" +
                    "where m.end_date is not null) combined\n" +
                    "group by year(combined.paid_at), month(combined.paid_at)\n" +
                    "order by year(combined.paid_at), month(combined.paid_at)",
            nativeQuery = true
    )
    List<Object[]> getMonthlyRevenue();
}
