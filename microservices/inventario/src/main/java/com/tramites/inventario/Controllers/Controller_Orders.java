package com.tramites.inventario.Controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tramites.inventario.DTO.Component.*;
import com.tramites.inventario.DTO.CompraRequestDTO;
import com.tramites.inventario.Services.CatalogProduct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@CrossOrigin(origins = "*", methods =  {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/App/Orders")
public class Controller_Orders {
    private static final Logger log = LoggerFactory.getLogger(Controller_Orders.class);
    final CatalogProduct catalogproduct;

    public Controller_Orders(CatalogProduct catalogproduct) {

        this.catalogproduct = catalogproduct;

    }

 @PostMapping("/input")
 public ResponseEntity<?> createOrder(@RequestBody CompraRequestDTO order ){
         var salida =catalogproduct.SendOrderQueue(order);
         return ResponseEntity
             .status(HttpStatus.ACCEPTED)
             .body(salida);

 }

 @DeleteMapping("/event/delete")
 public ResponseEntity<?> deleteEvent(@RequestBody DeleteDetailEvent event){
      var salida =catalogproduct.SendEventoQueue(event);
        return ResponseEntity
             .status(HttpStatus.NO_CONTENT)
             .body(salida);
 }

 @PostMapping("/series/search")
public ResponseEntity<?> getSeries(@Valid @RequestBody Choices choices){
     var salida =catalogproduct.findQuery(choices);
     if (salida.isEmpty()){
         throw new NotFoundException("No se encontraron órdenes con los filtros proporcionados");
     }
     return ResponseEntity
             .status(HttpStatus.OK)
             .body(salida);

 }

@DeleteMapping("/item/delete")
public ResponseEntity<?> deleteItems( @Valid @RequestBody List<HeadDetail> items ){
    var salida =catalogproduct.deteleitem(items);
    var salida2=items.stream().map(i->i.getProductid()).collect(Collectors.toList());
    var  eliminados = salida.stream().allMatch(Boolean::booleanValue);
    if ( ! eliminados) {
        return ResponseEntity.badRequest().body("No se pudo eliminar todos los  items "+salida2);

    }
    return ResponseEntity.noContent().build();
}


@PostMapping("/item")
public ResponseEntity<?> createItems(@RequestBody List<HeadDetail> items ){
    var salida=catalogproduct.InsertDetail(items);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("created: "+salida);
}

/*
@DeleteMapping("/item/delete")
public ResponseEntity<?> DeletetItems(@RequestBody List<HeadDetail> items ){
    var salida=catalogproduct.InsertDetail(items);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("created: "+salida);
}
*/
@PostMapping("/onsite")
public ResponseEntity <?> insertProduct(@RequestBody CompraRequestDTO request) throws JsonProcessingException {


    var suma=catalogproduct.CompraTotal(request);
    request.setTotalCompra(suma);
    request.setExternal_id(UUID.randomUUID().toString());

    var salida =catalogproduct.Insert(request);

    var json=catalogproduct.ConvertJSON(salida);
    log.info("Salida json {}",json);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(json);

}



@DeleteMapping("/head/delete")
public ResponseEntity<?> deleteheader(@RequestBody List<HeadDetail > Orders){
    var salida= catalogproduct.deleteheader(Orders);
    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body("Delete: "+Orders);
    }
}
