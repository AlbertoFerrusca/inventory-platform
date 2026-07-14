package com.retail.payments.repocitory;

import com.retail.payments.DTO.PaymentFilter;
import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.service.ServicePayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class Repository_Payment {
    private static final Logger log = LoggerFactory.getLogger(Repository_Payment.class);
   final JdbcTemplate jdbcTemplate;

    public Repository_Payment(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String  preparateStatement(PaymentFilter filter, List<Object> params){
      StringBuilder sql = new StringBuilder("SELECT payment_id,order_id,amount,status,payment_method,");
              sql.append("installments,estado, created_at FROM public.payments WHERE 1=1");
      if (filter.getOrder_id() != null && !filter.getOrder_id().isEmpty()){
          params.add(filter.getOrder_id());
          sql = new StringBuilder(" and order_id = ?");
          return sql.toString();
      }

// ✅ PRIORIDAD 2: payment_id
      if (filter.getPayment_id() != null) {
          sql.append(" AND payment_id = ?");
          params.add(UUID.fromString(filter.getPayment_id()));
      }

      // ✅ status opcional
      if (filter.getStatus() != null) {
          sql.append(" AND status = ?");
          params.add(filter.getStatus());
      }

      // ✅ mes actual
      sql.append(" AND DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE)");

     return sql.toString();
  }
  public List<PaymentRequest> FindPayment(PaymentFilter filter){
       List<Object> params=new  ArrayList<> ();
       var sqlQuery=preparateStatement(filter, params);
      List<PaymentRequest> result = jdbcTemplate.query(
              sqlQuery,
              params.toArray(),
              (rs, rowNum) -> {

                  return new PaymentRequest(
                          rs.getString("order_id"), //uuuvalue
                          rs.getDouble("amount"),
                          rs.getString("currency"),
                          rs.getString("status"),
                          rs.getString("payment_method"),
                          rs.getString("branch_id"),
                          rs.getString("estado"),
                          rs.getString("bank_folio"),
                          rs.getInt("installments")
                  );

              });

      return result;
  }

  private String  preparateStatemenRetry(PaymentRequest paymentRequest,List<Object> params,String msg) {
      var sqlStatment=new StringBuilder("Select fn_error_payment(?,?,?,?,?,?,?,?)");
      params.add(UUID.fromString(paymentRequest.getOrder_id())); //uui
      params.add(BigDecimal.valueOf(paymentRequest.getAmount()));
      params.add(paymentRequest.getStatus());
      params.add(paymentRequest.getPayment_method());
      params.add(paymentRequest.getInstallments());
      params.add(paymentRequest.getEstado());
      params.add(paymentRequest.getBank_folio());
      params.add(msg);
      return sqlStatment.toString();
    }
  private String  preparateStatementInsert(PaymentRequest paymentRequest,List<Object> params){
    var sqlStatment=new StringBuilder("Select fn_insert_payment(?,?,?,?,?,?,?)");
      params.add(UUID.fromString(paymentRequest.getOrder_id())); //uui
      params.add(BigDecimal.valueOf(paymentRequest.getAmount()));
      params.add(paymentRequest.getStatus());
      params.add(paymentRequest.getPayment_method());
      params.add(paymentRequest.getInstallments());
      params.add(paymentRequest.getEstado());
      params.add(paymentRequest.getBank_folio());

      return sqlStatment.toString();
  }
  public String   InsertPayment(PaymentRequest paymentRequest){
      List<Object> params=new  ArrayList<> ();
        var sqlQuery=preparateStatementInsert(paymentRequest, params);
        log.info("sqlQueryInsertNormal : {}",sqlQuery);
        log.info("paramssqlQueryInsertNormal : {}",params);

        UUID paymentId = jdbcTemplate.queryForObject(sqlQuery,params.toArray(), UUID.class);

        return (paymentId != null ? paymentId.toString() : null);
  }
    private  int InsertPaymentError(PaymentRequest paymentRequest,String error) {
        List<Object> params=new  ArrayList<> ();
        var sqlQuery=preparateStatemenRetry(paymentRequest, params,error);
        log.info("sqlQuery: {}",sqlQuery);
        log.info("params: {}",params);
        return jdbcTemplate.update(sqlQuery,params.toArray());
    }
   public int SaveError(PaymentRequest paymentRequest,String  error){
       return InsertPaymentError(paymentRequest,error);
   }

}
