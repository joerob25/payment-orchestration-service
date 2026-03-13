package io.vosy.payment_orchestration_service.domain;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
    }
}
