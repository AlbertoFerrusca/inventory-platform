package com.retail.payments.controller;

import com.retail.payments.DTO.PaymentFilter;
import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.service.ServicePayment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)

public class PaymentControllertest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ServicePayment service;
    @Test
    void savePaymentOk() throws Exception {
        when(service.PaymentSave(any())).thenReturn(1);
        mockMvc.perform(post("/api/v1/payments/async").contentType(MediaType.APPLICATION_JSON)
                .content("""
                   {"order_id":"c1f8a7e2-6d94-4b3a-8e5f-2d71c9ab4f36","amount":511.50,
                                      "status":"PENDING","payment_method":"CARD",
                        "installments":12,"estado":"Jalisco","bank_folio":"ABC123456"}""")).andExpect(status().isOk());}
    @Test
    void savePaymentError() throws Exception {
       when(service.PaymentSave(any())).thenReturn(-1);
       mockMvc.perform(post("/api/v1/payments/async").contentType(MediaType.APPLICATION_JSON)
               .content("""
                  {"order_id":"c1f8a7e2-6d94-4b3a-8e5f-2d71c9ab4f36","amount":511.50}""")).andExpect(status()
               .isBadRequest());}


}
