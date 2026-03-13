package io.vosy.payment_orchestration_service.application.service;

import io.vosy.payment_orchestration_service.application.exception.InvalidCurrencyException;
import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentCommand;
import io.vosy.payment_orchestration_service.application.port.in.PaymentResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InitiatePaymentServiceTest {

    private final InitiatePaymentService service = new InitiatePaymentService();

    @Nested
    class Initiate {
        @Test
        void shouldCreatePaymentAndReturnResult_withValidCommand() {
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "GBP",
                "tok_abc123"
            );

            PaymentResult result = service.initiate(command);

            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(result.status()).isEqualTo("PENDING");
        }

        @Test
        void shouldThrowException_whenCurrencyIsInvalid() {
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "INVALID",
                "tok_abc123"
            );

            assertThatThrownBy(() -> service.initiate(command))
                .isInstanceOf(InvalidCurrencyException.class);
        }

        @Test
        void shouldAcceptValidISO4217Currencies() {
            String[] validCurrencies = {"GBP", "USD", "EUR", "JPY", "AUD", "CAD"};

            for (String currency : validCurrencies) {
                InitiatePaymentCommand command = new InitiatePaymentCommand(
                    BigDecimal.valueOf(100),
                    currency,
                    "tok_test"
                );

                PaymentResult result = service.initiate(command);

                assertThat(result.status()).isEqualTo("PENDING");
            }
        }

        @Test
        void shouldGenerateUniquePaymentIds() {
            InitiatePaymentCommand command1 = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "GBP",
                "tok_abc123"
            );
            InitiatePaymentCommand command2 = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "GBP",
                "tok_xyz789"
            );

            PaymentResult result1 = service.initiate(command1);
            PaymentResult result2 = service.initiate(command2);

            assertThat(result1.paymentId()).isNotEqualTo(result2.paymentId());
        }

        @Test
        void shouldRespectInputAmount() {
            BigDecimal amount = BigDecimal.valueOf(50.25);
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                amount,
                "GBP",
                "tok_abc123"
            );

            PaymentResult result = service.initiate(command);

            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isNotNull();
        }

        @Test
        void shouldRespectInputCardToken() {
            String cardToken = "tok_special_token";
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "GBP",
                cardToken
            );

            PaymentResult result = service.initiate(command);

            assertThat(result).isNotNull();
            assertThat(result.paymentId()).isNotNull();
        }

        @Test
        void shouldThrowException_whenCurrencyCodeIsEmpty() {
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "",
                "tok_abc123"
            );

            assertThatThrownBy(() -> service.initiate(command))
                .isInstanceOf(InvalidCurrencyException.class);
        }

        @Test
        void shouldThrowException_whenCurrencyCodeIsLowercase() {
            InitiatePaymentCommand command = new InitiatePaymentCommand(
                BigDecimal.valueOf(99.99),
                "gbp",
                "tok_abc123"
            );

            assertThatThrownBy(() -> service.initiate(command))
                .isInstanceOf(InvalidCurrencyException.class);
        }
    }
}
