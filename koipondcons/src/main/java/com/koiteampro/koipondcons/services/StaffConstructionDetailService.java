package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffConstructionDetailService {
    @Autowired
    private StaffConstructionDetailRepository staffConstructionDetailRepository;

    public List<StaffConstructionDetail> findStaffConstructionDetailByConstructionOrderId(long constructionOrderId) {
        return  staffConstructionDetailRepository.findAllByConstructionOrderId(constructionOrderId);
    }
}
