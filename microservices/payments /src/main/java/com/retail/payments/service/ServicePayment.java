package com.retail.payments.service;

import com.retail.payments.DTO.PaymentFilter;
import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.controller.PaymentController;
import com.retail.payments.messaging.PayingProducer;
import com.retail.payments.repocitory.Repository_Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.function.EntityResponse;

import java.sql.SQLException;
import java.util.List;

@Service
public class ServicePayment {
    private static final Logger log = LoggerFactory.getLogger(ServicePayment.class);
    private  final PayingProducer payingProducer;
    private final Repository_Payment repositoryPaymentent;
    private final ErrorService errorService;

    public ServicePayment(Repository_Payment repositoryPaymentent,  PayingProducer payingProducer,ErrorService errorService) {
        this.repositoryPaymentent = repositoryPaymentent;
        this.payingProducer=payingProducer;
        this.errorService=errorService;
    }


    @Transactional
   public String  PaymentSave(PaymentRequest paymentRequest){
       try {
         var result=repositoryPaymentent.InsertPayment(paymentRequest);
          if (result ==null) {
              errorService.saveError(paymentRequest,"Error al guardar el pago");
              return "-1";
          }
           return result;
       } catch(DataAccessException e) {
           Throwable cause = e.getMostSpecificCause();
           if (cause instanceof SQLException sqlEx) {
               String error = String.format("SQLState: %s, ErrorCode: %s, Mensaje: %s",
                       sqlEx.getSQLState(), sqlEx.getErrorCode(), sqlEx.getMessage());
           }
           errorService.saveError(paymentRequest, e.getMostSpecificCause().getMessage());
           return "-1";
       }

       }



    @Transactional(readOnly = true)
    public List<PaymentRequest> FindPayment(PaymentFilter paymentFilter){
       return repositoryPaymentent.FindPayment(paymentFilter);

    }
   public void sendPayment(PaymentRequest request){
        payingProducer.sendPayment(request);
    }

}
