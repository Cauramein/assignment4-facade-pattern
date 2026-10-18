package com.ecommerce.subsystem;

public class NotificationService {
    public void sendOrderConfirmation(String email, String orderId, String trackingNumber) {
        System.out.printf("[NotificationService] Order confirmation sent to %s for order %s (Tracking: %s)%n",
                email, orderId, trackingNumber);
    }

    public void sendOrderFailureAlert(String email, String reason) {
        System.out.printf("[NotificationService] Order failure alert sent to %s: %s%n", email, reason);
    }
}