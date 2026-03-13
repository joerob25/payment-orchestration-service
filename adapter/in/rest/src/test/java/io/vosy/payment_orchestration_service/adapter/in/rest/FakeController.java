package io.vosy.payment_orchestration_service.adapter.in.rest;

import io.vosy.payment_orchestration_service.application.exception.InvalidCurrencyException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class FakeController {

    record FakeRequest(
            @NotBlank String fieldA,
            @NotBlank String fieldB
    ) {}

    @PostMapping("/invalid-currency")
    void throwInvalidCurrency() {
        throw new InvalidCurrencyException("XYZ");
    }

    @PostMapping("/error")
    void throwError() {
        throw new RuntimeException("unexpected");
    }

    @PostMapping("/validated")
    void validated(@Valid @RequestBody FakeRequest request) {}
}
