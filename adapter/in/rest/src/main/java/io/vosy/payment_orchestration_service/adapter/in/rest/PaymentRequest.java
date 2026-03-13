package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Incoming payment request")
public record PaymentRequest(
        @NotNull
        @Positive
        @Schema(description = "Payment amount", example = "99.99")
        BigDecimal amount,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 code (3 uppercase letters)")
        @Schema(description = "ISO 4217 currency code", example = "GBP")
        String currency,

        @NotBlank
        @Size(min = 5, max = 100, message = "Card token must be between 5 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Card token must contain only alphanumeric characters, hyphens, and underscores")
        @Schema(description = "Tokenised card reference", example = "tok_abc123")
        String cardToken
) {
}
