package io.vosy.payment_orchestration_service.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    private static final Currency GBP = Currency.getInstance("GBP");
    private static final PaymentId PAYMENT_ID = PaymentId.generate();
    private static final Money MONEY = new Money(BigDecimal.valueOf(99.99), GBP);
    private static final CardToken CARD_TOKEN = new CardToken("tok_abc123");

    @Nested
    class Constructor {
        @Test
        void shouldCreatePaymentWithInitialPendingStatus() {
            Payment payment = new Payment(PAYMENT_ID, MONEY, CARD_TOKEN);

            assertThat(payment.id()).isEqualTo(PAYMENT_ID);
            assertThat(payment.money()).isEqualTo(MONEY);
            assertThat(payment.cardToken()).isEqualTo(CARD_TOKEN);
            assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
        }

        @Test
        void shouldThrowException_whenIdIsNull() {
            assertThatThrownBy(() -> new Payment(null, MONEY, CARD_TOKEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment ID cannot be null");
        }

        @Test
        void shouldThrowException_whenMoneyIsNull() {
            assertThatThrownBy(() -> new Payment(PAYMENT_ID, null, CARD_TOKEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money cannot be null");
        }

        @Test
        void shouldThrowException_whenCardTokenIsNull() {
            assertThatThrownBy(() -> new Payment(PAYMENT_ID, MONEY, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card token cannot be null");
        }
    }

    @Nested
    class Accessors {
        @Test
        void shouldReturnThePaymentId() {
            Payment payment = new Payment(PAYMENT_ID, MONEY, CARD_TOKEN);

            assertThat(payment.id()).isEqualTo(PAYMENT_ID);
        }

        @Test
        void shouldReturnTheMoney() {
            Payment payment = new Payment(PAYMENT_ID, MONEY, CARD_TOKEN);

            assertThat(payment.money()).isEqualTo(MONEY);
        }

        @Test
        void shouldReturnTheCardToken() {
            Payment payment = new Payment(PAYMENT_ID, MONEY, CARD_TOKEN);

            assertThat(payment.cardToken()).isEqualTo(CARD_TOKEN);
        }

        @Test
        void shouldReturnPendingStatus() {
            Payment payment = new Payment(PAYMENT_ID, MONEY, CARD_TOKEN);

            assertThat(payment.status()).isEqualTo(PaymentStatus.PENDING);
        }
    }
}
