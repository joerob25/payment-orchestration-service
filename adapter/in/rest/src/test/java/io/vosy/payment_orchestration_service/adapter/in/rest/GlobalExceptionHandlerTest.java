package io.vosy.payment_orchestration_service.adapter.in.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new FakeController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Nested
    class HandleInvalidCurrency {
        @Test
        void shouldReturn400_withErrorMessage() throws Exception {
            mockMvc.perform(post("/test/invalid-currency")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("XYZ")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.path", is("/test/invalid-currency")));
        }
    }

    @Nested
    class HandleValidationException {
        @Test
        void shouldReturn422_withFieldErrors() throws Exception {
            mockMvc.perform(post("/test/validated")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"fieldA\": \"value\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status", is(422)))
                .andExpect(jsonPath("$.error", is("Unprocessable Entity")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors.fieldB", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
        }

        @Test
        void shouldReturn422_withAllFieldErrors_whenMultipleFieldsInvalid() throws Exception {
            mockMvc.perform(post("/test/validated")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.fieldA", notNullValue()))
                .andExpect(jsonPath("$.errors.fieldB", notNullValue()));
        }
    }

    @Nested
    class HandleGeneralException {
        @Test
        void shouldReturn500_withGenericMessage() throws Exception {
            mockMvc.perform(post("/test/error")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", is("Internal Server Error")))
                .andExpect(jsonPath("$.message", is("An unexpected error occurred")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }
}
