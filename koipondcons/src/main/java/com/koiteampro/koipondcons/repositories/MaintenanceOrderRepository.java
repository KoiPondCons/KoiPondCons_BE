package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
    MaintenanceOrder findByConstructorAccountIdAndStatusLike(long constructorAccount_id, String status);
//    List<MaintenanceOrder> findAllByConstructorAccountIdAndStatusNotLike(MaintenanceOrderStatus status);
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndCustomer(LocalDate now, Customer customer);
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndConsultantAccount(LocalDate now, Account consultant);
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndStatusAndConstructorAccount(LocalDate createAt, MaintenanceOrderStatus status, Account constructorAccount);

    @Query(
            "select m \n" +
            "from MaintenanceOrder m\n" +
            "where m.status != 'REQUESTED'" +
            "and m.status != 'PENDING'"
    )
    List<MaintenanceOrder> getAllConfirmedMaintenanceOrders();

    @Query(
            "select m \n" +
            "from MaintenanceOrder m\n" +
            "where m.status = 'REQUESTED'" +
            "and m.createAt <= now()"
    )
    List<MaintenanceOrder> getAllRequestedMaintenanceOrders();

    @Query(
            "select m.constructorAccount.id\n" +
            "from MaintenanceOrder m\n" +
            "where m.constructorAccount.id is not null\n" +
            "and m.status = 'PROCESSING'"
    )
    List<Long> findStaffIdsWithUnfinishedWorks();
}
