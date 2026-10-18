package com.ecommerce.subsystem;

import java.util.UUID;

public class ShippingService {
    public String scheduleDelivery(String address, String orderId) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Shipping address cannot be empty.");
        }
        String tracking = "TRACK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.printf("[ShippingService] Delivery scheduled for order %s to '%s'. Tracking: %s%n",
                orderId, address, tracking);
        return tracking;
    }

    public void cancelDelivery(String trackingNumber) {
        System.out.printf("[ShippingService] Delivery canceled for tracking: %s%n", trackingNumber);
    }
}