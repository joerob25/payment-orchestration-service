package io.vosy.payment_orchestration_service.domain;

public record CardToken(String value) {

    public CardToken {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Card token must not be blank");
        }
    }
}
