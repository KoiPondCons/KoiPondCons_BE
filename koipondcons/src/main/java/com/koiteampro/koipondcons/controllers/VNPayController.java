package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.models.response.SubmitPaymentResponse;
import com.koiteampro.koipondcons.services.ConsOrderPaymentService;
import com.koiteampro.koipondcons.services.MaintenanceOrderService;
import com.koiteampro.koipondcons.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private ConsOrderPaymentService consOrderPaymentService;

    @Autowired
    private MaintenanceOrderService maintenanceOrderService;


//    @PostMapping("/submitOrder")
//    public String submitOrder(@RequestParam("amount") int orderTotal,
//                            @RequestParam("orderInfo") String orderInfo,
//                            HttpServletRequest request){
////        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + "5173";
//        String baseUrl = "http://localhost:5173";
//
//        return vnPayService.createOrder(request, orderTotal*100, orderInfo, baseUrl);
//    }

    @PostMapping("/submitOrder/{paymentId}")
    public String submitOrder(@PathVariable String paymentId,
                              HttpServletRequest request){

        ConsOrderPayment consOrderPayment = consOrderPaymentService.getConsOrderPaymentById(Long.parseLong(paymentId));

        String baseUrl = "http://localhost:5173";

        return vnPayService.createOrder(request, consOrderPayment.getAmount().longValueExact()*100, "order/" + String.valueOf(consOrderPayment.getId()), baseUrl);
    }

    @PostMapping("/submitMaintainOrder/{paymentId}")
    public String submitMaintainOrder(@PathVariable String paymentId,
                              HttpServletRequest request){

        MaintenanceOrderResponse maintenanceOrderResponse = maintenanceOrderService.getByMaintenanceOrderId(Long.parseLong(paymentId));

        String baseUrl = "https://koipondcons.onrender.com";

        return vnPayService.createOrder(request, (long)maintenanceOrderResponse.getPrice()*100, "maintain/" + String.valueOf(maintenanceOrderResponse.getId()), baseUrl);
    }

    @GetMapping("/vnpay-payment-return")
    public SubmitPaymentResponse GetMapping(HttpServletRequest request){
        return vnPayService.returnSubmitOrder(request);
    }
}
