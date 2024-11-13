package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import org.hibernate.annotations.Array;
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

    @Autowired
    StaffConstructionDetailRepository staffConstructionDetailRepository;

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
}
