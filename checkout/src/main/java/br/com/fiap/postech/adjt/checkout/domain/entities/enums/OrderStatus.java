package br.com.fiap.postech.adjt.checkout.domain.entities.enums;

public enum OrderStatus {
    PENDING("pending"),
    APPROVED("approved"),
    ECLINED("declined");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
