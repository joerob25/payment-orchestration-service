package io.vosy.payment_orchestration_service.application.port.in;

import java.util.UUID;

public record PaymentResult(UUID paymentId, String status) {
}
