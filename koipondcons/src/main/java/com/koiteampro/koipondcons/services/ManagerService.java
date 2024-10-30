package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConstructionOrderRepository constructionOrderRepository;

    @Autowired
    MaintenanceOrderRepository maintenanceOrderRepository;

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    ConsOrderPaymentRepository consOrderPaymentRepository;

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

        List<Object[]> totalOrdersByCombo = comboRepository.countOrdersOfCombo();
        List<Map<String, Object>> totalOrdersByComboList = new ArrayList<>();

        for (Object[] row : totalOrdersByCombo) {
            Map<String, Object> comboInfo = new HashMap<>();
            comboInfo.put("comboName", row[0]);
            comboInfo.put("totalOrders", row[1]);
            totalOrdersByComboList.add(comboInfo);
        }
        stats.put("totalOrdersByCombo", totalOrdersByComboList);

        return stats;
    }

    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Map<String, Object>> monthlyRevenueByYears = new ArrayList<>();

        Map<Object, List<Map<String, Object>>> yearMap = new HashMap<>();

        List<Object[]> getListFromRepo = consOrderPaymentRepository.getMonthlyRevenue();
        for (Object[] row : getListFromRepo) {
            Map<String, Object> monthInfo = new HashMap<>();
            monthInfo.put("month", row[1]);
            monthInfo.put("revenue", row[2]);

            if (!yearMap.containsKey(row[0])) {
                List<Map<String, Object>> monthList = new ArrayList<>();
                monthList.add(monthInfo);
                yearMap.put(row[0], monthList);
            }
            else {
                yearMap.get(row[0]).add(monthInfo);
            }
        }

        for (Map.Entry<Object, List<Map<String, Object>>> entry : yearMap.entrySet()) {
            Map<String, Object> yearInfo = new HashMap<>();
            yearInfo.put("year", entry.getKey());
            yearInfo.put("monthlyRevenue", entry.getValue());
            monthlyRevenueByYears.add(yearInfo);
        }

        return monthlyRevenueByYears;
    }
}
