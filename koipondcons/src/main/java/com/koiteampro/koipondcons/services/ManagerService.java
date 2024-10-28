package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.MaintenanceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConstructionOrderRepository constructionOrderRepository;

    @Autowired
    MaintenanceOrderRepository maintenanceOrderRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalCustomerAccounts = accountRepository.countByIsEnabledTrueAndRole(Role.CUSTOMER);
        stats.put("totalCustomerAccounts", totalCustomerAccounts);

        long totalCustomersUsedService = constructionOrderRepository.countCustomersUsedService();
        stats.put("totalCustomersUsedService", totalCustomersUsedService);

        long totalConstructionProjects = constructionOrderRepository.countByStatus(ConstructionOrderStatus.CLOSED);
        stats.put("totalConstructionProjects", totalConstructionProjects);

        long totalMaintenanceOrders = maintenanceOrderRepository.countByStatus(MaintenanceOrderStatus.FINISHED);
        stats.put("totalMaintenanceOrders", totalMaintenanceOrders);

        return stats;
    }
}
