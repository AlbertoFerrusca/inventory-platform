package com.tramites.inventario.Respository;

import com.tramites.inventario.DTO.CompraItemDTO;
import com.tramites.inventario.DTO.Component.*;
import com.tramites.inventario.DTO.CompraRequestDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;


@Repository
public class ProductRespository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(ProductRespository.class);

    public ProductRespository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String StatementInsertHead(List<Object> params,CompraRequestDTO dto) {
        if ( dto.getOrderId() != null && ! dto.getOrderId().isBlank() && ! dto.getOrderId().isEmpty()   ) {
            params.add(dto.getOrderId());
            params.add(dto.getEmployId());
            params.add(dto.getEstatus());
            params.add(dto.getCliente());
            params.add(dto.getTotalCompra());
            params.add(dto.getExternal_id());
            return """
                    Insert into public.headproduct(OrderID,EmployId,Estatus,ClienteID,Total,external_id)
                     values (?,?,?,?,?,?)
                     RETURNING Orderid""";

        }

        params.add(dto.getEmployId());
        params.add(dto.getEstatus());
        params.add(dto.getCliente());
        params.add(dto.getTotalCompra());
        params.add(dto.getExternal_id());
        return """
                Insert into public.headproduct(EmployId,Estatus,ClienteID,Total,external_id)
                 values (?,?,?,?,?)
                 RETURNING Orderid""";

    }

    private String StatementInsertDetail() {
        return """
                Insert into public.detailproduct(orderid,productid,quantity,price)
                   values (?,?,?,?) """;


    }

    private String StatementUpdateHead(CompraRequestDTO Dto) {
        return """ 
                update public.HeadProduct
                 set EmployId = ? ;
                     Fecha = ? , 
                     Estatus = ? , 
                     ClienteID = ? , 
                    Total = ?  
                Where  IdOrder = ? 
                and estatus='DRAFTED'
                """;
    }

    private String StatementUpdateDetail(CompraRequestDTO Dto) {
        return """
                update public.DetailProduct d
                set  Quantity= ? , 
                 Price = ? , 
                Where  d.IdOrder = ? and 
                d.ProductID= ? and
                Exists  (Select 1
                from public.headProduct h
                Where d.orderid=H.orderid
                and h.estatus='DRAFTED') 
                 """;

    }

    private String StatementDEleteHeader() {
        return  """
                Select fn_delete_head(?)
        """;

    }

    private String StatementDEleteDetail() {
        return """
                Select fn_delete_item(?,?) 
                """;

    }

    private CompraRequestDTO Prepareorder(CompraRequestDTO order) {
        double total = order.getItems().
                stream().mapToDouble(i -> i.getCantidad() * i.getPrecio())
                .sum();

        order.setTotalCompra(total);
        order.setEstatus("process");
        return order;
    }


    private String SelectRowsQuery(Choices choices,List<Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                Select h.OrderID,h.EmployId,h.Fecha,h.Estatus,h.ClienteID,h.Total,
                d.ProductID,d.Quantity,d.price
                from public.headproduct h
                inner join public.detailproduct d on h.OrderID=d.OrderID
                Where 1=1 
                """);

        if (choices.getEmployId() != null && !choices.getEmployId().isBlank()) {
            sb.append(" AND h.EmployId = ? ");
            params.add(choices.getEmployId());
        }
        if (choices.getFecha1() != null && choices.getFecha2() != null) {
            sb.append(" AND h.Fecha::date between ? and ? ");
            params.add(choices.getFecha1());
            params.add(choices.getFecha2().plusDays(1));
        }
        if (choices.getOrderID() != null ) {
            sb.append(" AND h.orderID = ? ");
            params.add(choices.getOrderID());
        }

        if (choices.getClienteID() != null && !choices.getClienteID().isBlank()) {
            sb.append(" AND h.ClienteID = ? ");
            params.add(choices.getClienteID());
        }

        if (choices.getEstatus() != null && !choices.getEstatus().isEmpty()) {

            String placeholders = choices.getEstatus().stream()
                    .map(s -> "?")
                    .collect(Collectors.joining(","));

            sb.append(" AND h.Estatus IN (" + placeholders + ") ");

            params.addAll(choices.getEstatus());
        }



//and ClienteID= ?
        return sb.toString();
    }


    private String getDetailStatement(){
       return """
               Select  productid,quantity,price
               from public.detailproduct
               Where orderid= ?;
               """;
    }

  private String SelectRowsStatement(){
      return """
               Select orderid,employid,fecha,estatus,clienteid,total,external_id
               from public.headproduct
               Where orderid = ?
               """;
  }

    public List<CompraItemDTO> Fetchdetail(String orderId){
        List<Object> params = new ArrayList<>();
        var sqlStatement=getDetailStatement();
        log.info("sqlStatement {} ",sqlStatement);
        return  jdbcTemplate.query(
                sqlStatement,
                new Object[]{UUID.fromString(orderId)},
                (rs, rowNum) ->
                          new CompraItemDTO(
                            rs.getString("productid"),
                            rs.getDouble("quantity"),
                            rs.getDouble("price"))

                );


    }
    public CompraRequestDTO InsertHead(CompraRequestDTO dto) {
        List<Object> params = new ArrayList<>();
        var insertHead = StatementInsertHead(params,dto);


        var headnumber = jdbcTemplate.queryForObject(insertHead,
                params.toArray(),
                String.class);
        dto.setOrderId(headnumber);
        return dto;
    }


    public int IInsertDetail(HeadDetail dto){
        var insertDetail = StatementInsertDetail();
        return jdbcTemplate.update(insertDetail, new Object[]{UUID.fromString(dto.getOrderID()),dto.getProductid() , dto.getCantidad(), dto.getPrecio()});

    }
    public void IInsertDetail(CompraRequestDTO dto, String orderNum) {
        var insertDetail = StatementInsertDetail();
        for (var item : dto.getItems()) {
            jdbcTemplate.update(insertDetail, new Object[]{UUID.fromString(orderNum), item.getProductoId(), item.getCantidad(), item.getPrecio()});
        }
    }

    public List<CompraRequestDTO> findRows(String orderNumber) {
        List<Object> params = new ArrayList<>();
        String sql = SelectRowsStatement();
        var order =UUID.fromString(orderNumber);
        List<CompraRequestDTO> result = jdbcTemplate.query(
                sql,
                new Object[]{order},
                (rs, rowNum) -> {
                    LocalDateTime fecha1 = rs.getObject("fecha", LocalDateTime.class);
                    return new CompraRequestDTO(
                            rs.getString("orderid"),
                            rs.getString("clienteid"),
                            rs.getString("employid"),
                            fecha1,
                            rs.getString("estatus"),
                            rs.getDouble("total"),
                            rs.getString("external_id"));

                });
        return result;
    }

 public List<HeadDetail> RowQuerySelection(Choices  choices){
     List<Object> params=new ArrayList<>();
        var sqlquery=SelectRowsQuery(choices,params);
        List<HeadDetail> result = jdbcTemplate.query(
             sqlquery,
                params.toArray(),
             (rs, rowNum) -> {
                 LocalDateTime fecha1 = rs.getObject("Fecha", LocalDateTime.class);
                 return new HeadDetail(
                         rs.getString("OrderID"),
                         rs.getString("ClienteID"),
                         rs.getString("EmployId"),
                         fecha1,
                         rs.getString("Estatus"),
                         rs.getString("productId"),
                         rs.getDouble("Quantity"),
                         rs.getDouble("price")
                         );

             });

         return result;
 }

 public  List<Boolean>   deleteItems(List <HeadDetail> headDetail) {
    var deleteStatement=StatementDEleteDetail();
    return headDetail.stream().map(p->
            jdbcTemplate.queryForObject(
                    deleteStatement,
                    new Object[]{UUID.fromString(p.getOrderID()),
                  p.getProductid()},
     Boolean.class)).toList();
    }

public  List<Boolean>   deleteHeader(List <HeadDetail> headDetail) {
    var deleteStatement= StatementDEleteHeader();
   return headDetail.stream().map(p->
           jdbcTemplate.queryForObject(deleteStatement,
                   new Object[]{UUID.fromString(p.getOrderID())},
                   Boolean.class)).toList();

}

}


