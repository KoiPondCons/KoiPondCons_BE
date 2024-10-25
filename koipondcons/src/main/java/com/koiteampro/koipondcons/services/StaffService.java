package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.models.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffService {

    @Autowired
    MaintenanceOrderService maintenanceOrderService;

    @Autowired
    StaffConstructionDetailService staffConstructionDetailService;

    public List<AccountResponse> getALlFreeConstructor() {
        List<AccountResponse> staffsNotInMaintaining = maintenanceOrderService.getConstructorsNotInMaintaining();
        List<AccountResponse> staffsNotInConstructing = staffConstructionDetailService.getConstructorsNotInConstructing();
        List<AccountResponse> freeConstructors = new ArrayList<>(staffsNotInMaintaining);
        freeConstructors.retainAll(staffsNotInConstructing);
        return freeConstructors;
    }
}
