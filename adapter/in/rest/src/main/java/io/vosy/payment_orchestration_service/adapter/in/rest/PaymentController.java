package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Payments", description = "Payment initiation and management")
public interface PaymentController {

    @Operation(summary = "Initiate a card payment", description = "Accepts a payment request and routes it to the appropriate payment provider")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Payment accepted and queued for processing"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "422", description = "Validation failed - invalid request parameters")
    })
    PaymentResponse initiatePayment(@Valid @RequestBody PaymentRequest request);
}
