package io.vosy.payment_orchestration_service.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    private static final Currency GBP = Currency.getInstance("GBP");
    private static final Currency USD = Currency.getInstance("USD");

    @Nested
    class Constructor {
        @Test
        void shouldCreateMoneyWithValidAmountAndCurrency() {
            BigDecimal amount = BigDecimal.valueOf(99.99);
            Money money = new Money(amount, GBP);

            assertThat(money.amount()).isEqualByComparingTo(amount);
            assertThat(money.currency()).isEqualTo(GBP);
        }

        @Test
        void shouldThrowException_whenAmountIsNull() {
            assertThatThrownBy(() -> new Money(null, GBP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
        }

        @Test
        void shouldThrowException_whenAmountIsZero() {
            assertThatThrownBy(() -> new Money(BigDecimal.ZERO, GBP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
        }

        @Test
        void shouldThrowException_whenAmountIsNegative() {
            assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-50), GBP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
        }

        @Test
        void shouldThrowException_whenCurrencyIsNull() {
            assertThatThrownBy(() -> new Money(BigDecimal.valueOf(100), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency must not be null");
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.01", "1.00", "100.50", "999999.99"})
        void shouldAcceptValidPositiveAmounts(String amount) {
            Money money = new Money(new BigDecimal(amount), GBP);

            assertThat(money.amount()).isEqualByComparingTo(amount);
        }
    }

    @Nested
    class Equality {
        @Test
        void shouldBeBasedOnAmountAndCurrency() {
            BigDecimal amount = BigDecimal.valueOf(50);
            Money money1 = new Money(amount, GBP);
            Money money2 = new Money(amount, GBP);

            assertThat(money1).isEqualTo(money2);
        }

        @Test
        void shouldBeDifferentForDifferentCurrencies() {
            BigDecimal amount = BigDecimal.valueOf(50);
            Money money1 = new Money(amount, GBP);
            Money money2 = new Money(amount, USD);

            assertThat(money1).isNotEqualTo(money2);
        }

        @Test
        void shouldBeDifferentForDifferentAmounts() {
            Money money1 = new Money(BigDecimal.valueOf(50), GBP);
            Money money2 = new Money(BigDecimal.valueOf(51), GBP);

            assertThat(money1).isNotEqualTo(money2);
        }
    }
}
