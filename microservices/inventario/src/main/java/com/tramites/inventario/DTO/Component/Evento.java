package com.tramites.inventario.DTO.Component;

import java.util.Objects;

public class Evento {
  private Long id;
  private Long aggregateId;
  private String eventType;
  private String payload;
  private String status;
  private int retryCount;

        // getters y setters


    public Evento(Long id, Long aggregateId, String eventType, String payload, String status, int retryCount) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.retryCount = retryCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(Long aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return retryCount == evento.retryCount && Objects.equals(id, evento.id) && Objects.equals(aggregateId, evento.aggregateId) && Objects.equals(eventType, evento.eventType) && Objects.equals(payload, evento.payload) && Objects.equals(status, evento.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aggregateId, eventType, payload, status, retryCount);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", aggregateId=" + aggregateId +
                ", eventType='" + eventType + '\'' +
                ", payload='" + payload + '\'' +
                ", status='" + status + '\'' +
                ", retryCount=" + retryCount +
                '}';
    }
}
