package io.vosy.payment_orchestration_service.domain;

public class Payment {

    private final PaymentId id;
    private final Money money;
    private final CardToken cardToken;
    private PaymentStatus status;

    public Payment(PaymentId id, Money money, CardToken cardToken) {
        if (id == null) {
            throw new IllegalArgumentException("Payment ID cannot be null");
        }
        if (money == null) {
            throw new IllegalArgumentException("Money cannot be null");
        }
        if (cardToken == null) {
            throw new IllegalArgumentException("Card token cannot be null");
        }
        this.id = id;
        this.money = money;
        this.cardToken = cardToken;
        this.status = PaymentStatus.PENDING;
    }

    public PaymentId id() {
        return id;
    }

    public Money money() {
        return money;
    }

    public CardToken cardToken() {
        return cardToken;
    }

    public PaymentStatus status() {
        return status;
    }
}
