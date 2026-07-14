package com.tramites.inventario.DTO.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class HeadDetail {
    @JsonProperty("orderId")
    private String orderId;
    private String cliente;
    private String employId;
    private LocalDateTime fecha;
    private String estatus;
    @NotNull
    private String productid;
    private double cantidad=0;
    private double precio=0;

    public HeadDetail(){}
    public HeadDetail(String orderId, String cliente, String employId, LocalDateTime fecha, String estatus, String productId, double cantidad, double precio) {
        this.orderId = orderId;
        this.cliente = cliente;
        this.employId = employId;
        this.fecha = fecha;
        this.estatus = estatus;
        this.productid = productId;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getOrderID() {
        return orderId;
    }

    public void setOrderID(long orderID) {
        orderId = orderId;
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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productId) {
        this.productid = productId;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "HeadDetail{" +
                "orderId=" + orderId +
                ", cliente='" + cliente + '\'' +
                ", employId='" + employId + '\'' +
                ", fecha=" + fecha +
                ", estatus='" + estatus + '\'' +
                ", productId='" + productid + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HeadDetail that = (HeadDetail) o;
        return orderId.equals(that.orderId) && Double.compare(cantidad, that.cantidad) == 0 && Double.compare(precio, that.precio) == 0 && Objects.equals(cliente, that.cliente) && Objects.equals(employId, that.employId) && Objects.equals(fecha, that.fecha) && Objects.equals(estatus, that.estatus) && Objects.equals(productid, that.productid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, cliente, employId, fecha, estatus, productid, cantidad, precio);
    }
}
