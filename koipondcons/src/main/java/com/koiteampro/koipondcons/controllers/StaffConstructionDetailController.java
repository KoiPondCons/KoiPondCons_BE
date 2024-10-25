package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import com.koiteampro.koipondcons.models.request.StaffConstructionDetailUpdateRequest;
import com.koiteampro.koipondcons.services.StaffConstructionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class StaffConstructionDetailController {

    @Autowired
    private StaffConstructionDetailService staffConstructionDetailService;

    @GetMapping("/staffconstructiondetail/{id}")
    public ResponseEntity<List<StaffConstructionDetail>> getStaffConstructionDetailByID(@PathVariable long id) {
        return ResponseEntity.ok(staffConstructionDetailService.findStaffConstructionDetailByConstructionOrderId(id));
    }

    @PutMapping("/staffconstructiondetail/{id}")
    public  ResponseEntity updateStaffConstructionDetail(@PathVariable long id, @RequestBody StaffConstructionDetailUpdateRequest staffConstructionDetailUpdateRequest) {
        return ResponseEntity.ok(staffConstructionDetailService.updateStaffConstructionDetail(id, staffConstructionDetailUpdateRequest));
    }
}
