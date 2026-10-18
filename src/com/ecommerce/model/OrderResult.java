package com.ecommerce.model;

public record OrderResult(boolean success, String orderId, String trackingNumber, String message) {
    public static OrderResult ok(String orderId, String trackingNumber) {
        return new OrderResult(true, orderId, trackingNumber, "Order processed successfully.");
    }

    public static OrderResult failure(String message) {
        return new OrderResult(false, null, null, message);
    }
}