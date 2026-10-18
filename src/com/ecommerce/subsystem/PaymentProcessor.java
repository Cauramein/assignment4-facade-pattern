package com.ecommerce.subsystem;

public class PaymentProcessor {
    public boolean processPayment(String customerId, double amount, String paymentToken) {
        if (paymentToken == null || paymentToken.isBlank() || paymentToken.equals("EXPIRED_TOKEN")) {
            System.err.printf("[PaymentProcessor] Payment failed for customer %s on $%.2f%n", customerId, amount);
            return false;
        }
        System.out.printf("[PaymentProcessor] Successfully billed $%.2f to customer %s (Token: %s)%n",
                amount, customerId, paymentToken);
        return true;
    }

    public void refundPayment(String customerId, double amount) {
        System.out.printf("[PaymentProcessor] Refunded $%.2f to customer %s%n", amount, customerId);
    }
}