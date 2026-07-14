package com.tramites.inventario.DTO.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class OrderEventDTO {
    @NotNull
private EventType type;

@NotBlank
private String orderId;

private String productId;

    public OrderEventDTO(){}

    public OrderEventDTO(EventType type, String orderId, String productId) {
        this.type = type;
        this.orderId = orderId;
        this.productId = productId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderEventDTO that = (OrderEventDTO) o;
        return type == that.type && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, orderId, productId);
    }

    @Override
    public String toString() {
        return "OrderEventDTO{" +
                "type=" + type +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
