package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.sun.tools.javac.Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
    MaintenanceOrder findByConstructorAccountAndStatus(Account constructorAccount, MaintenanceOrderStatus status);
    List<MaintenanceOrder> findMaintenanceOrderByRequestDateBeforeAndCustomer(LocalDateTime now, Customer customer);
    List<MaintenanceOrder> findMaintenanceOrderByRequestDateBeforeAndConsultantAccount(LocalDateTime now, Account consultant);
    MaintenanceOrder findMaintenanceOrderById(long maintenanceOrderId);
    long countByStatus(MaintenanceOrderStatus status);

    @Query(
            "select m \n" +
            "from MaintenanceOrder m\n" +
            "where m.constructorAccount = :constructorAccount" +
            " and m.status = 'PROCESSED' or m.status = 'FINISHED'"
    )
    List<MaintenanceOrder> getFinishedMaintenanceOrderOfConstructor(Account constructorAccount);

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
            "and m.requestDate <= now()"
    )
    List<MaintenanceOrder> getAllRequestedMaintenanceOrders();

    @Query(
            "select m.constructorAccount.id\n" +
            "from MaintenanceOrder m\n" +
            "where m.constructorAccount.id is not null\n" +
            "and m.status = 'PROCESSING'"
    )
    List<Long> findStaffIdsWithUnfinishedWorks();
    List<MaintenanceOrder> findMaintenanceOrdersByStatusNot(MaintenanceOrderStatus status);
}
