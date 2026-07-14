package com.retail.payments.service;

import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.controller.PaymentController;
import com.retail.payments.repocitory.Repository_Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Service

public class ErrorService {
    private static final Logger log = LoggerFactory.getLogger(Error.class);
    private final Repository_Payment repositoryPaymentent;
    public ErrorService(Repository_Payment repositoryPaymentent) {
        this.repositoryPaymentent=repositoryPaymentent;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveError(PaymentRequest paymentRequest, String message) {
        StringBuilder error=new StringBuilder(" ");
        try {
            repositoryPaymentent.SaveError(paymentRequest, message);
        }
        catch (DataAccessException e) {
            Throwable cause = e.getMostSpecificCause();
            if (cause instanceof SQLException sqlEx) {
                error.append("SQLState: ").append(sqlEx.getSQLState()).append(" ErrorCode: ")
                        .append(sqlEx.getErrorCode()).
                        append(" Mensaje: ").append(sqlEx.getMessage());
             }
         log.error("No fue posible guardar el log : {}",error.toString() );
        }
    }
}
