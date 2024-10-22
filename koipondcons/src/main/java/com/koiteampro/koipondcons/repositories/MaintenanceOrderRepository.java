package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndCustomer(LocalDate now, Customer customer);
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndConsultantAccount(LocalDate now, Account consultant);
    List<MaintenanceOrder> findMaintenanceOrderByCreateAtBeforeAndStatusAndConstructorAccount(LocalDate createAt, MaintenanceOrderStatus status, Account constructorAccount);
}
