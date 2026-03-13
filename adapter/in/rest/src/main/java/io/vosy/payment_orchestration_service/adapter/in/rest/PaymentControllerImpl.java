package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentCommand;
import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
class PaymentControllerImpl implements PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentControllerImpl.class);

    private final InitiatePaymentUseCase initiatePayment;

    PaymentControllerImpl(InitiatePaymentUseCase initiatePayment) {
        this.initiatePayment = initiatePayment;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PaymentResponse initiatePayment(@RequestBody PaymentRequest request) {
        logger.info("Payment initiation requested: amount={}, currency={}", request.amount(), request.currency());

        var command = new InitiatePaymentCommand(
                request.amount(),
                request.currency(),
                request.cardToken()
        );
        var result = initiatePayment.initiate(command);

        logger.info("Payment initiated successfully: paymentId={}, status={}", result.paymentId(), result.status());
        return new PaymentResponse(result.paymentId(), result.status());
    }
}
