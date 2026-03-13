package io.vosy.payment_orchestration_service.application.exception;

public class InvalidCurrencyException extends RuntimeException {

    private final String currencyCode;

    public InvalidCurrencyException(String currencyCode) {
        super("Invalid currency code: " + currencyCode);
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
