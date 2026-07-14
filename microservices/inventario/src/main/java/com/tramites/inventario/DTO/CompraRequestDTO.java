package com.tramites.inventario.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CompraRequestDTO {
   @JsonProperty("orderId")
 private String orderId;
 private String cliente;
 private String employId;
 private LocalDateTime fecha;
 private String estatus;
 private double totalCompra;
 @JsonProperty("external_id")
 private String external_id;
 private List<CompraItemDTO> items;


    public CompraRequestDTO(){}
    public CompraRequestDTO(String orderId, String cliente, String employId, LocalDateTime fecha, String estatus,  double totalCompra,String external_id) {
        this.orderId = orderId;
        this.cliente = cliente;
        this.employId = employId;
        this.fecha = fecha;
        this.estatus = estatus;
        this.totalCompra = totalCompra;
        this.external_id = external_id;
    }
    public CompraRequestDTO(String orderId,String cliente, String employId, LocalDateTime fecha, String estatus,  double totalCompra,String external_id ,List<CompraItemDTO> items) {
        this.orderId = orderId;
        this.cliente = cliente;
        this.employId = employId;
        this.fecha = fecha;
        this.estatus = estatus;
        this.totalCompra = totalCompra;
        this.external_id = external_id;
        this.items = items;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmployId() {
        return employId;
    }

    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }


    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public List<CompraItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CompraItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraRequestDTO that = (CompraRequestDTO) o;
        return orderId.equals(orderId) && Double.compare(totalCompra, that.totalCompra) == 0 && Objects.equals(cliente, that.cliente) && Objects.equals(employId, that.employId) && Objects.equals(fecha, that.fecha) && Objects.equals(estatus, that.estatus) && Objects.equals(external_id, that.external_id) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, cliente, employId, fecha, estatus, totalCompra, external_id, items);
    }

    @Override
    public String toString() {
        return "CompraRequestDTO{" +
                "orderId=" + orderId +
                ", cliente='" + cliente + '\'' +
                ", employId='" + employId + '\'' +
                ", fecha=" + fecha +
                ", estatus='" + estatus + '\'' +
                ", totalCompra=" + totalCompra +
                ", external_id='" + external_id + '\'' +
                ", items=" + items +
                '}';
    }
}
