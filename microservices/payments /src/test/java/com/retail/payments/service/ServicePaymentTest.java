package com.retail.payments.service;

import com.retail.payments.DTO.PaymentFilter;
import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.messaging.PayingProducer;
import com.retail.payments.repocitory.Repository_Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class ServicePaymentTest {
    @Mock
    private Repository_Payment repositoryPaymentent;
    @Mock
    private PayingProducer payingProducer;
    @Mock
    private ErrorService errorService;
    @InjectMocks
    private ServicePayment service;

    @Test
    void paymentSaveOk() {
        PaymentRequest request = new PaymentRequest();
        when(repositoryPaymentent.InsertPayment(any())).thenReturn(1);
        int result = service.PaymentSave(request);
        assertEquals(1, result);
        verify(errorService, never()).saveError(any(), anyString());
    }

    @Test
    void paymentSaveInsertError() {
        PaymentRequest request = new PaymentRequest();
        when(repositoryPaymentent.InsertPayment(any())).thenReturn(0);
        int result = service.PaymentSave(request);
        assertEquals(-1, result);
        verify(errorService).saveError(eq(request), anyString());
    }

    @Test
    void paymentSaveDataAccessException() {
        PaymentRequest request = new PaymentRequest();

        when(repositoryPaymentent.InsertPayment(any())).thenThrow(new DataAccessResourceFailureException("DB Error"));
        int result = service.PaymentSave(request);
        assertEquals(-1, result);
        verify(errorService).saveError(eq(request), anyString());
    }
    @Test
    void findPaymentOk() {
        PaymentFilter filter = new PaymentFilter();
        List<PaymentRequest> expected = List.of(new PaymentRequest());
        when(repositoryPaymentent.FindPayment(filter)).thenReturn(expected);
        var result = service.FindPayment(filter);
        assertEquals(expected, result);
    }
    @Test
    void sendPaymentOk() {
        PaymentRequest request = new PaymentRequest();
        service.sendPayment(request);
        verify(payingProducer).sendPayment(request);
    }
}