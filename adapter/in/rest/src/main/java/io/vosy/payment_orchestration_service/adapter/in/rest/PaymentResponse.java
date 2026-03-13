package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Payment initiation response")
public record PaymentResponse(
        @Schema(description = "Unique payment identifier") UUID paymentId,
        @Schema(description = "Current payment status", example = "PENDING") String status
) {
}
