package io.vosy.payment_orchestration_service.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentIdTest {

    @Nested
    class Generate {
        @Test
        void shouldCreateNewPaymentIdWithRandomUUID() {
            PaymentId id1 = PaymentId.generate();
            PaymentId id2 = PaymentId.generate();

            assertThat(id1.value()).isNotNull();
            assertThat(id2.value()).isNotNull();
            assertThat(id1.value()).isNotEqualTo(id2.value());
        }
    }

    @Nested
    class Of {
        @Test
        void shouldCreatePaymentIdWithGivenUUID() {
            UUID uuid = UUID.randomUUID();
            PaymentId id = PaymentId.of(uuid);

            assertThat(id.value()).isEqualTo(uuid);
        }

        @Test
        void shouldThrowException_whenValueIsNull() {
            assertThatThrownBy(() -> new PaymentId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PaymentId value cannot be null");
        }
    }

    @Nested
    class Equality {
        @Test
        void shouldBeBasedOnUUIDValue() {
            UUID uuid = UUID.randomUUID();
            PaymentId id1 = PaymentId.of(uuid);
            PaymentId id2 = PaymentId.of(uuid);

            assertThat(id1).isEqualTo(id2);
        }

        @Test
        void shouldBeDifferentForDifferentUUIDs() {
            PaymentId id1 = PaymentId.generate();
            PaymentId id2 = PaymentId.generate();

            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
