package com.tramites.inventario.DTO.Component;

import java.util.List;
import java.util.Objects;

public class DeleteDetailEvent {
    private String type;
    private String orderId;
    private List<String> items;

    public DeleteDetailEvent(){}
    public DeleteDetailEvent(String type, String orderId, List<String> items) {
        this.type = type;
        this.orderId = orderId;
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderid() {
        return orderId;
    }

    public void setOrderid(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeleteDetailEvent that = (DeleteDetailEvent) o;
        return Objects.equals(type, that.type) && Objects.equals(orderId, that.orderId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, orderId, items);
    }

    @Override
    public String toString() {
        return "DeleteDetailEvent{" +
                "type='" + type + '\'' +
                ", orderId='" + orderId + '\'' +
                ", items=" + items +
                '}';
    }
}
