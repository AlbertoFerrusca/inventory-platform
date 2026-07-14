package com.tramites.inventario.Controllers;

import com.tramites.inventario.Configuration.LoggingInterceptor;
import com.tramites.inventario.DTO.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@CrossOrigin(origins="*.*",methods={RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/App")
public class ControllerUsers {
   private static final Logger log = LogManager.getLogger(ControllerUsers.class);

   @PostMapping(value="/data_ent",
           consumes= MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Person> IngresaDatos(@RequestBody Person dto){
      log.info(dto.toString());
      return ResponseEntity.ok().body(dto);
   }
}

