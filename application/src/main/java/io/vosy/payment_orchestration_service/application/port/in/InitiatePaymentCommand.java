package io.vosy.payment_orchestration_service.application.port.in;

import java.math.BigDecimal;

public record InitiatePaymentCommand(
        BigDecimal amount,
        String currency,
        String cardToken
) {
}
