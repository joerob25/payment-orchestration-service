package io.vosy.payment_orchestration_service.domain;

import java.util.UUID;

public record PaymentId(UUID value) {

    public PaymentId {
        if (value == null) throw new IllegalArgumentException("PaymentId value cannot be null");
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }

    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }
}
