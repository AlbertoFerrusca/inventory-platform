package com.retail.payments.controller;

import com.retail.payments.DTO.PaymentFilter;
import com.retail.payments.DTO.PaymentRequest;

import com.retail.payments.service.ServicePayment;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods =  {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api/v1/payments")
public class PaymentController {


    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final ServicePayment service;


    public PaymentController(ServicePayment service) {

        this.service = service;

    }


    @PostMapping("async")
    public ResponseEntity<?> SavePayment(@Valid @RequestBody PaymentRequest request){
        log.info("Saving payment: {}", request);
        var payment = service.PaymentSave(request);
        if (payment.equals("-1")) {
            return ResponseEntity.badRequest().body("Verificar tablas de Logs");
        }

        return ResponseEntity.ok().body(request);
    }

    @GetMapping("/query")
    public ResponseEntity<?> FindPayment(PaymentFilter paymentFilter) {

        var payment = service.FindPayment(paymentFilter);

        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(payment);
    }

    @PostMapping("/queue")
    public ResponseEntity<PaymentRequest> sendToQueue(@Valid @RequestBody PaymentRequest request){
        log.info("Sending payment to queue: {}", request);
        service.sendPayment(request);
        return ResponseEntity.accepted().body(request);
    }
}





