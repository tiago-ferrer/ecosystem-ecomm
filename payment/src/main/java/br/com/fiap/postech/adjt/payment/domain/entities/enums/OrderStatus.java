package br.com.fiap.postech.adjt.payment.domain.entities.enums;

public enum OrderStatus {
    pending("pending"),
    approved("approved"),
    declined("declined");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
