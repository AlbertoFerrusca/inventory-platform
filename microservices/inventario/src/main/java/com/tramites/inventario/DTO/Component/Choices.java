package com.tramites.inventario.DTO.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Choices {

    private String employId;
    private LocalDate fecha1;
    private LocalDate fecha2;
    private String clienteID;
    private List<String> estatus ;
    private String orderID;

    public Choices(){}

    public Choices(String employId, LocalDate fecha1, LocalDate fecha2, String clienteID,List<String> estatus,String orderID) {
        this.employId = employId;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.clienteID = clienteID;
        this.estatus = estatus;
        this.orderID = orderID;
    }

    public List<String> getEstatus() {
        return estatus;
    }

    public void setEstatus(List<String> estatus) {
        this.estatus = estatus;
    }

    public String getEmployId() {
        return employId;
    }

    public void setEmployId(String employId) {
        this.employId = employId;
    }

    public LocalDate getFecha1() {
        return fecha1;
    }

    public void setFecha1(LocalDate fecha1) {
        this.fecha1 = fecha1;
    }

    public LocalDate getFecha2() {
        return fecha2;
    }

    public void setFecha2(LocalDate fecha2) {
        this.fecha2 = fecha2;
    }

    public String getClienteID() {
        return clienteID;
    }

    public void setClienteID(String clienteID) {
        this.clienteID = clienteID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Choices choices = (Choices) o;
        return Objects.equals(employId, choices.employId) && Objects.equals(fecha1, choices.fecha1) && Objects.equals(fecha2, choices.fecha2) && Objects.equals(clienteID, choices.clienteID) && Objects.equals(estatus, choices.estatus) && Objects.equals(orderID, choices.orderID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employId, fecha1, fecha2, clienteID, estatus, orderID);
    }

    @Override
    public String toString() {
        return "Choices{" +
                "employId='" + employId + '\'' +
                ", fecha1=" + fecha1 +
                ", fecha2=" + fecha2 +
                ", clienteID='" + clienteID + '\'' +
                ", estatus=" + estatus +
                ", orderID=" + orderID +
                '}';
    }
}
