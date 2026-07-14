package com.tramites.inventario.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tramites.inventario.Configuration.Queues.OrdersIN.OrderProducer;
import com.tramites.inventario.Configuration.Queues.Events.EvenProducer;
import com.tramites.inventario.DTO.Component.Choices;
import com.tramites.inventario.DTO.Component.DeleteDetailEvent;
import com.tramites.inventario.DTO.Component.Evento;
import com.tramites.inventario.DTO.Component.HeadDetail;
import com.tramites.inventario.DTO.CompraItemDTO;
import com.tramites.inventario.DTO.CompraRequestDTO;
import com.tramites.inventario.Respository.ProductRespository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.UUID;

@Service
public class CatalogProduct {
private final ProductRespository productRespository;
private final OrderProducer orderProducer;
private final EvenProducer evenProducer;
private static final Logger log = LoggerFactory.getLogger(CatalogProduct.class);

    public CatalogProduct(ProductRespository productRespository,OrderProducer orderProducer,EvenProducer evenProducer) {
        this.productRespository = productRespository;
        this.orderProducer=orderProducer;
        this.evenProducer=evenProducer;
    }

  public DeleteDetailEvent SendEventoQueue(DeleteDetailEvent evento){
      evenProducer.sendEvent(evento);
      return evento;
  }
  public CompraRequestDTO SendOrderQueue(CompraRequestDTO order){
      String externalId = UUID.randomUUID().toString();
      var suma=CompraTotal(order);
      order.setTotalCompra(suma);
      order.setExternal_id(UUID.randomUUID().toString());
      orderProducer.sendOrder(order);
      return order;
  }
    public List<CompraRequestDTO> findID(String id){
        List<CompraRequestDTO> orden=productRespository.findRows(id);
        var listProducts=productRespository.Fetchdetail(id);
        orden.get(0).setItems(listProducts);
        return orden;
    }

    @Transactional
 public CompraRequestDTO Insert(CompraRequestDTO order){
        var orderNum = productRespository.InsertHead(order);
        log.info("orderNum {} ",orderNum);
        productRespository.IInsertDetail(order, orderNum.getOrderId());

     List<CompraRequestDTO> orden=productRespository.findRows(orderNum.getOrderId());
     var listProducts=productRespository.Fetchdetail(orderNum.getOrderId());
     return orderNum;
    }


  public List<HeadDetail> findQuery(Choices choices){
        return  productRespository.RowQuerySelection(choices);

    }

 @Transactional
    public List<Boolean> deteleitem(List <HeadDetail> headDetail){
        return productRespository.deleteItems(headDetail);
    }
@Transactional
    public int InsertDetail(List<HeadDetail> items){
        var cont=0;
        for (var item : items) {
            cont+=productRespository.IInsertDetail(item);
        }
        return cont;
    }
 public double  CompraTotal(CompraRequestDTO request){
      return request.getItems().stream().mapToDouble(i->i.getCantidad()*i.getPrecio()).sum();
 }
@Transactional
public List<Boolean> deleteheader(List<HeadDetail> items){
    return productRespository.deleteHeader(items);
}

public String ConvertJSON(CompraRequestDTO request) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
    }

}
