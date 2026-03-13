package io.vosy.payment_orchestration_service.application.port.in;

public interface InitiatePaymentUseCase {

    PaymentResult initiate(InitiatePaymentCommand command);
}
