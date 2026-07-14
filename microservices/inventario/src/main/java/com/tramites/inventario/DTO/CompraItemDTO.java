package com.tramites.inventario.DTO;

import java.util.Objects;

public class CompraItemDTO {
    private String productoId;
    private double cantidad;
    private double precio;

    public CompraItemDTO(){}
    public CompraItemDTO(String productoId, double cantidad, double precio) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
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

    public String toString() {
        return "CompraItemDTO [productoId=" + productoId + ", cantidad=" + cantidad + ", precio=" + precio + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraItemDTO that = (CompraItemDTO) o;
        return cantidad == that.cantidad && Double.compare(precio, that.precio) == 0 && Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, cantidad, precio);
    }
}
