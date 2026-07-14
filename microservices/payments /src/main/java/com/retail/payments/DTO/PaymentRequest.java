package com.retail.payments.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

public class PaymentRequest {

    @NotNull(message = "order_id es obligatorio")
    private String order_id;

    @NotNull(message = "amount es obligatorio")
    @Positive(message = "amount debe ser mayor a 0")
    private Double amount;

    private String currency = "MXN";

    @NotBlank(message = "status es obligatorio")
    private String status;
    @NotBlank(message = "payment_method es obligatorio")
    private String payment_method;

    @NotBlank(message = "branch_id es obligatorio")
    private String branch_id;

    private String estado;

    private String bank_folio;

    @NotNull(message = "installments es obligatorio")
    @Min(value = 1, message = "installments mínimo 1")
    private int installments;

    public PaymentRequest(){}
    public PaymentRequest(String order_id, Double amount, String currency, String status, String payment_method, String branch_id, String estado, String bank_folio, int installments) {
        this.order_id = order_id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.payment_method = payment_method;
        this.branch_id = branch_id;
        this.estado = estado;
        this.bank_folio = bank_folio;
        this.installments = installments;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBank_folio() {
        return bank_folio;
    }

    public void setBank_folio(String bank_folio) {
        this.bank_folio = bank_folio;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return installments == that.installments && Objects.equals(order_id, that.order_id) && Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency) && Objects.equals(status, that.status) && Objects.equals(payment_method, that.payment_method) && Objects.equals(branch_id, that.branch_id) && Objects.equals(estado, that.estado) && Objects.equals(bank_folio, that.bank_folio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_id, amount, currency, status, payment_method, branch_id, estado, bank_folio, installments);
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "order_id='" + order_id + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", branch_id='" + branch_id + '\'' +
                ", estado='" + estado + '\'' +
                ", bank_folio='" + bank_folio + '\'' +
                ", installments=" + installments +
                '}';
    }
}