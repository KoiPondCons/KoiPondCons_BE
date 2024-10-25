package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.DesignDrawing;
import com.koiteampro.koipondcons.models.request.DesignDrawingRequest;
import com.koiteampro.koipondcons.services.DesignDrawingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class DesignDrawingController {

    @Autowired
    private DesignDrawingService designDrawingService;

    @PutMapping("/design-drawings/{id}")
    public ResponseEntity updateDesignDrawing(@PathVariable long id, @RequestBody DesignDrawingRequest designDrawingRequest) {
        designDrawingService.updateDesignDrawing(id, designDrawingRequest);
        return ResponseEntity.ok("Update design drawing successfully!");
    }

    @GetMapping("/design-drawings/{id}")
    public ResponseEntity getDesignDrawing(@PathVariable long id) {
        return ResponseEntity.ok(designDrawingService.getDesignDrawing(id));
    }

    @GetMapping("/design-drawings")
    public ResponseEntity getDesignDrawings() {
        return ResponseEntity.ok(designDrawingService.getAllDesignDrawings());
    }

    @GetMapping("/design-drawings/designer")
    public ResponseEntity getDesignDrawingByDesignerId() {
        return ResponseEntity.ok(designDrawingService.getAllDesignOfDesigner());
    }
}
