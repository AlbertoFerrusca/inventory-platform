package com.tramites.inventario.Services;

import com.tramites.inventario.Configuration.Queues.Events.EvenProducer;
import com.tramites.inventario.Configuration.Queues.OrdersIN.OrderProducer;
import com.tramites.inventario.DTO.Component.Choices;
import com.tramites.inventario.DTO.Component.DeleteDetailEvent;
import com.tramites.inventario.DTO.Component.HeadDetail;
import com.tramites.inventario.DTO.CompraItemDTO;
import com.tramites.inventario.DTO.CompraRequestDTO;
import com.tramites.inventario.Respository.ProductRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class CatalogProductTest {
    @Mock
    private ProductRespository productRespository;
    @Mock    private OrderProducer orderProducer;
    @Mock    private EvenProducer evenProducer;
    @InjectMocks
    private CatalogProduct catalogProduct;

    @Test
    void sendEventoQueueOk() {
        DeleteDetailEvent event = new DeleteDetailEvent();
        DeleteDetailEvent result = catalogProduct.SendEventoQueue(event);
        verify(evenProducer).sendEvent(event);
        assertEquals(event, result);
    }
    @Test
    void compraTotalOk() {
        CompraItemDTO item1 = new CompraItemDTO();
        item1.setCantidad(2);
        item1.setPrecio(10);
        CompraItemDTO item2 = new CompraItemDTO();
        item2.setCantidad(3);
        item2.setPrecio(20);
        CompraRequestDTO order = new CompraRequestDTO();
        order.setItems(List.of(item1, item2));
        double total = catalogProduct.CompraTotal(order);
        assertEquals(80, total);
    }

    @Test
    void sendOrderQueueOk() {
        CompraItemDTO item = new CompraItemDTO();
        item.setCantidad(2);
        item.setPrecio(50);
        CompraRequestDTO order = new CompraRequestDTO();
        order.setItems(List.of(item));
        CompraRequestDTO result = catalogProduct.SendOrderQueue(order);
        verify(orderProducer).sendOrder(order);
        assertEquals(100, result.getTotalCompra());
        assertNotNull(result.getExternal_id());
        assertFalse(result.getExternal_id().isBlank());
    }

    @Test
    void findIDOk() {
        String id = "ABC";
        CompraRequestDTO order = new CompraRequestDTO();
        List<CompraItemDTO> details = List.of(new CompraItemDTO());

        when(productRespository.findRows(id)).thenReturn(List.of(order));
        when(productRespository.Fetchdetail(id))
                .thenReturn(details);
        List<CompraRequestDTO> result =catalogProduct.findID(id);
        assertEquals(1, result.size());
        assertEquals(details, result.get(0).getItems());
        verify(productRespository).findRows(id);
        verify(productRespository).Fetchdetail(id);

    }
    @Test
    void findQueryOk() {
        Choices choices = new Choices();
        List<HeadDetail> expected =List.of(new HeadDetail());
        when(productRespository.RowQuerySelection(choices)).thenReturn(expected);
        List<HeadDetail> result =catalogProduct.findQuery(choices);
        assertEquals(expected, result);
        verify(productRespository).RowQuerySelection(choices);
    }
    @Test
    void deleteItemOk() {
        List<HeadDetail> items =List.of(new HeadDetail());
        when(productRespository.deleteItems(items)).thenReturn(List.of(true));
        List<Boolean> result =catalogProduct.deteleitem(items);
        assertEquals(List.of(true), result);
        verify(productRespository).deleteItems(items);
    }
 @Test
 void insertDetailOk() {
        HeadDetail item1 = new HeadDetail();
        HeadDetail item2 = new HeadDetail();
        when(productRespository.IInsertDetail(any(HeadDetail.class)))
        .thenReturn(1);
        int result = catalogProduct.InsertDetail(
               List.of(item1, item2));
        assertEquals(2, result);
        verify(productRespository,times(2))
                .IInsertDetail(any(HeadDetail.class));
    }

    @Test
void deleteHeaderOk() {
List<HeadDetail> items = List.of(new HeadDetail());

        when(productRespository.deleteHeader(items))
        .thenReturn(List.of(true));

        List<Boolean> result =catalogProduct.deleteheader(items);
        assertEquals(List.of(true), result);
        verify(productRespository).deleteHeader(items);

    }
@Test
void convertJSONOk() throws Exception {
CompraRequestDTO order = new CompraRequestDTO();
order.setExternal_id("123");
String json = catalogProduct.ConvertJSON(
              order);
        assertNotNull(json);
        assertTrue(json.contains("123"));
}

@Test
void insertOk() {
        CompraRequestDTO order = new CompraRequestDTO();
        when(productRespository.InsertHead(order)).thenReturn("100");
        when(productRespository.findRows("100")).thenReturn(List.of(order));
        when(productRespository.Fetchdetail("100")).thenReturn(List.of(new CompraItemDTO()));
        CompraRequestDTO result =catalogProduct.Insert(order);
        assertEquals(order, result);
        verify(productRespository).InsertHead(order);
        verify(productRespository).IInsertDetail(order, "100");
    }
}
