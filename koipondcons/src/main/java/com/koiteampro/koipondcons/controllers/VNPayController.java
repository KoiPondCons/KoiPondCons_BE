package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RestController
@CrossOrigin("*")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;


    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                            @RequestParam("orderInfo") String orderInfo,
                            HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + "5173";
        return vnPayService.createOrder(request, orderTotal*100, orderInfo, baseUrl);
    }

    @GetMapping("/vnpay-payment-return")
    public String GetMapping(HttpServletRequest request){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}
