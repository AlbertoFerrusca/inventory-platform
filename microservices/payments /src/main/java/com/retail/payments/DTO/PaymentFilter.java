package com.retail.payments.DTO;

import java.util.Objects;

public class PaymentFilter {
    private String payment_id;
    private String order_id;
    private String status;

    public PaymentFilter(){}
    public PaymentFilter(String payment_id, String order_id, String status) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.status = status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentFilter that = (PaymentFilter) o;
        return Objects.equals(payment_id, that.payment_id) && Objects.equals(order_id, that.order_id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payment_id, order_id, status);
    }



    @Override
    public String toString() {
        return "PaymentFilter{" +
                "payment_id='" + payment_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
