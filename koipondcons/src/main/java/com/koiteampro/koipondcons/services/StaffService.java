package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StaffService {

    @Autowired
    MaintenanceOrderService maintenanceOrderService;

    @Autowired
    StaffConstructionDetailService staffConstructionDetailService;

    @Autowired
    StaffConstructionDetailRepository staffConstructionDetailRepository;

    @Autowired
    DesignDrawingService designDrawingService;

    public List<AccountResponse> getAllFreeConstructor() {
        List<AccountResponse> staffsNotInMaintaining = maintenanceOrderService.getConstructorsNotInMaintaining();
        List<AccountResponse> staffsNotInConstructing = staffConstructionDetailService.getConstructorsNotInConstructing();
        List<AccountResponse> freeConstructors = new ArrayList<>(staffsNotInMaintaining);
        freeConstructors.retainAll(staffsNotInConstructing);
        return freeConstructors;
    }

    public long countFreeConstructor() {
        return getAllFreeConstructor().size();
    }

    public Map<String, Object> staffStats() {
        int countDesigner = designDrawingService.getAllFreeDesigners().size();
        int countConstructor = getAllFreeConstructor().size();
        Map<String, Object> staffStats = new HashMap<>();
        staffStats.put("freeDesigner", countDesigner);
        staffStats.put("freeConstructor", countConstructor);
        return staffStats;
    }
}
