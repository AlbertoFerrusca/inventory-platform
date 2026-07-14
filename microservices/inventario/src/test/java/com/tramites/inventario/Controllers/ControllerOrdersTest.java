package com.tramites.inventario.Controllers;
import com.tramites.inventario.DTO.Component.DeleteDetailEvent;
import com.tramites.inventario.DTO.Component.HeadDetail;
import com.tramites.inventario.DTO.CompraRequestDTO;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.tramites.inventario.Services.CatalogProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.Collections;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ControllerOrdersTestService {

}
@AutoConfigureMockMvc
@WebMvcTest(Controller_Orders.class)
public class ControllerOrdersTest {
    @MockBean
    private CatalogProduct catalogProduct;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void createOrderOk() throws Exception {
        CompraRequestDTO order=new CompraRequestDTO();
        when(catalogProduct.SendOrderQueue(any())).thenReturn(order);
        mockMvc.perform(post("/App/Orders/input").
                contentType(MediaType.APPLICATION_JSON).content("""
                {}
                """)).andExpect(status().isAccepted());
    }

    @Test
    void deleteEventOk() throws Exception {
        DeleteDetailEvent deleteDetailEvent=new DeleteDetailEvent();
        when(catalogProduct.SendEventoQueue(any())).thenReturn(deleteDetailEvent);
        mockMvc.perform(delete("/App/Orders/event/delete").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {}
                        """)).andExpect(status().isNoContent());
    }

    @Test
    void getSeriesOk() throws Exception {
        when(catalogProduct.findQuery(any())).thenReturn(List.of(new HeadDetail()));
        mockMvc.perform(post("/App/Orders/series/search")
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
    }
    @Test
    void getSeriesNotFound() throws Exception {
        when(catalogProduct.findQuery(any())).
                thenThrow(new RuntimeException("Error"));
        mockMvc.perform(post("/App/Orders/series/search").
                contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void deleteItemsOk() throws Exception {
        when(catalogProduct.deteleitem(any())).
                thenReturn(List.of(true, true));
        mockMvc.perform(delete("/App/Orders/item/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""                
                        [
                         {
                         "orderID":1,
                         "productid":"ABC"
                          }
                         ]
                         """
                )).andExpect(status().isNoContent());
    }

    @Test
    void deleteItemsError() throws Exception {
        when(catalogProduct.deteleitem(any())).thenReturn(List.of(true, false));
        mockMvc.perform(delete("/App/Orders/item/delete").
                contentType(MediaType.APPLICATION_JSON).content("""
                        [
                        {
                        "orderID":1,
                        "productid":"ABC"
                        }
                        ]
                        """)).andExpect(status().isBadRequest());
    }

    @Test
    void createItemsOk() throws Exception {
        when(catalogProduct.InsertDetail(any())).thenReturn(1);
        mockMvc.perform(post("/App/Orders/item").
                contentType(MediaType.APPLICATION_JSON)
                .content("""
                     []
                     """)).andExpect(status().isCreated());
    }
    @Test
    void insertProductOk() throws Exception {
        when(catalogProduct.CompraTotal(any())).thenReturn(Double.MAX_VALUE);
        when(catalogProduct.Insert(any()))
                .thenReturn(new CompraRequestDTO());
        when(catalogProduct.ConvertJSON(any()))
                .thenReturn("{\"ok\":true}");
        mockMvc.perform(post("/App/Orders/onsite")
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated());
    }
    @Test
    void deleteHeaderOk() throws Exception {
        when(catalogProduct.deleteheader(any()))
                .thenReturn(List.of(true));
        mockMvc.perform(delete("/App/Orders/head/delete")
                .contentType(MediaType.APPLICATION_JSON).content("[]"))
                .andExpect(status().isNoContent());
    }

}
