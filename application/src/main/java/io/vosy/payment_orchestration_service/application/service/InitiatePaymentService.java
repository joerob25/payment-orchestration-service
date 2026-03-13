package io.vosy.payment_orchestration_service.application.service;

import io.vosy.payment_orchestration_service.application.exception.InvalidCurrencyException;
import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentCommand;
import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentUseCase;
import io.vosy.payment_orchestration_service.application.port.in.PaymentResult;
import io.vosy.payment_orchestration_service.domain.CardToken;
import io.vosy.payment_orchestration_service.domain.Money;
import io.vosy.payment_orchestration_service.domain.Payment;
import io.vosy.payment_orchestration_service.domain.PaymentId;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
class InitiatePaymentService implements InitiatePaymentUseCase {

    @Override
    public PaymentResult initiate(InitiatePaymentCommand command) {
        var currency = parseCurrency(command.currency());

        var payment = new Payment(
                PaymentId.generate(),
                new Money(command.amount(), currency),
                new CardToken(command.cardToken())
        );

        // TODO: persist payment, route to provider
        return new PaymentResult(payment.id().value(), payment.status().name());
    }

    private Currency parseCurrency(String currencyCode) {
        try {
            return Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException(currencyCode);
        }
    }
}
