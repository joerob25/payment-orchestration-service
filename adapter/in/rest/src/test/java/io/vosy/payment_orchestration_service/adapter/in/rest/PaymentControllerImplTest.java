package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentCommand;
import io.vosy.payment_orchestration_service.application.port.in.InitiatePaymentUseCase;
import io.vosy.payment_orchestration_service.application.port.in.PaymentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerImplTest {

    @Mock
    private InitiatePaymentUseCase initiatePaymentUseCase;

    @InjectMocks
    private PaymentControllerImpl controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Nested
    class InitiatePayment {
        @Test
        void shouldReturn202Accepted_withValidRequest() throws Exception {
            UUID paymentId = UUID.randomUUID();
            when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PaymentResult(paymentId, "PENDING"));

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.paymentId", is(paymentId.toString())))
                .andExpect(jsonPath("$.status", is("PENDING")));
        }

        @Test
        void shouldCallUseCaseWithCorrectCommand() throws Exception {
            UUID paymentId = UUID.randomUUID();
            when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PaymentResult(paymentId, "PENDING"));

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isAccepted());

            ArgumentCaptor<InitiatePaymentCommand> captor = ArgumentCaptor.forClass(InitiatePaymentCommand.class);
            verify(initiatePaymentUseCase).initiate(captor.capture());

            InitiatePaymentCommand command = captor.getValue();
            assertThat(command.amount()).isEqualByComparingTo("99.99");
            assertThat(command.currency()).isEqualTo("GBP");
            assertThat(command.cardToken()).isEqualTo("tok_abc123");
        }

        @Test
        void shouldReturnPaymentIdInResponse() throws Exception {
            UUID expectedPaymentId = UUID.randomUUID();
            when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PaymentResult(expectedPaymentId, "PENDING"));

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.paymentId", is(expectedPaymentId.toString())));
        }

        @Test
        void shouldReturnPaymentStatusInResponse() throws Exception {
            UUID paymentId = UUID.randomUUID();
            when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PaymentResult(paymentId, "PENDING"));

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status", is("PENDING")));
        }

        @Test
        void shouldReturnJSONWithCorrectContentType() throws Exception {
            UUID paymentId = UUID.randomUUID();
            when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new PaymentResult(paymentId, "PENDING"));

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", notNullValue()));
        }

        @Test
        void shouldAcceptValidCurrencyCodes() throws Exception {
            UUID paymentId = UUID.randomUUID();
            String[] validCurrencies = {"GBP", "USD", "EUR", "JPY", "AUD", "CAD"};

            for (String currency : validCurrencies) {
                when(initiatePaymentUseCase.initiate(org.mockito.ArgumentMatchers.any()))
                    .thenReturn(new PaymentResult(paymentId, "PENDING"));

                mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 99.99, \"currency\": \"" + currency + "\", \"cardToken\": \"tok_abc123\"}"))
                    .andExpect(status().isAccepted());
            }
        }
    }

    @Nested
    class Validation {
        @Test
        void shouldReturn422_whenAmountIsMissing() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenAmountIsNegative() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": -50.00, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenAmountIsZero() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 0, \"currency\": \"GBP\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCurrencyIsMissing() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCurrencyIsInvalidFormat() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GB\", \"cardToken\": \"tok_abc123\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCardTokenIsMissing() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCardTokenIsTooShort() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok\"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCardTokenIsBlank() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"    \"}"))
                .andExpect(status().isUnprocessableContent());
        }

        @Test
        void shouldReturn422_whenCardTokenContainsInvalidCharacters() throws Exception {
            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"amount\": 99.99, \"currency\": \"GBP\", \"cardToken\": \"tok@invalid!\"}"))
                .andExpect(status().isUnprocessableContent());
        }
    }
}
