package io.vosy.payment_orchestration_service.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTokenTest {

    @Nested
    class Constructor {
        @Test
        void shouldCreateCardTokenWithValidValue() {
            String token = "tok_abc123";
            CardToken cardToken = new CardToken(token);

            assertThat(cardToken.value()).isEqualTo(token);
        }

        @Test
        void shouldThrowException_whenValueIsNull() {
            assertThatThrownBy(() -> new CardToken(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card token must not be blank");
        }

        @Test
        void shouldThrowException_whenValueIsBlank() {
            assertThatThrownBy(() -> new CardToken("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card token must not be blank");
        }

        @Test
        void shouldThrowException_whenValueIsEmpty() {
            assertThatThrownBy(() -> new CardToken(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Card token must not be blank");
        }
    }

    @Nested
    class Equality {
        @Test
        void shouldBeBasedOnTokenValue() {
            String token = "tok_abc123";
            CardToken token1 = new CardToken(token);
            CardToken token2 = new CardToken(token);

            assertThat(token1).isEqualTo(token2);
        }

        @Test
        void shouldBeDifferentForDifferentTokens() {
            CardToken token1 = new CardToken("tok_abc123");
            CardToken token2 = new CardToken("tok_xyz789");

            assertThat(token1).isNotEqualTo(token2);
        }
    }
}
