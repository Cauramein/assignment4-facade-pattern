package com.ecommerce.facade;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.OrderResult;
import com.ecommerce.subsystem.InventoryService;
import com.ecommerce.subsystem.NotificationService;
import com.ecommerce.subsystem.PaymentProcessor;
import com.ecommerce.subsystem.ShippingService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderFacade {
    private final InventoryService inventoryService;
    private final PaymentProcessor paymentProcessor;
    private final ShippingService shippingService;
    private final NotificationService notificationService;

    public OrderFacade(InventoryService inventoryService,
                       PaymentProcessor paymentProcessor,
                       ShippingService shippingService,
                       NotificationService notificationService) {
        this.inventoryService = inventoryService;
        this.paymentProcessor = paymentProcessor;
        this.shippingService = shippingService;
        this.notificationService = notificationService;
    }

    public OrderFacade() {
        this(new InventoryService(), new PaymentProcessor(), new ShippingService(), new NotificationService());
    }

    public OrderResult placeOrder(Customer customer, List<CartItem> cart, String paymentToken) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        List<CartItem> reservedItems = new ArrayList<>();

        // 1. Reserve Inventory
        for (CartItem item : cart) {
            boolean reserved = inventoryService.reserveStock(item.productId(), item.quantity());
            if (!reserved) {
                rollbackInventory(reservedItems);
                notificationService.sendOrderFailureAlert(customer.email(), "Insufficient stock for: " + item.name());
                return OrderResult.failure("Item out of stock: " + item.name());
            }
            reservedItems.add(item);
        }

        // 2. Process Payment
        double total = cart.stream().mapToDouble(i -> i.unitPrice() * i.quantity()).sum();
        boolean billed = paymentProcessor.processPayment(customer.id(), total, paymentToken);
        if (!billed) {
            rollbackInventory(reservedItems);
            notificationService.sendOrderFailureAlert(customer.email(), "Payment authorization failed.");
            return OrderResult.failure("Payment declined. Inventory hold released.");
        }

        // 3. Schedule Shipping
        String trackingNumber;
        try {
            trackingNumber = shippingService.scheduleDelivery(customer.address(), orderId);
        } catch (Exception ex) {
            paymentProcessor.refundPayment(customer.id(), total);
            rollbackInventory(reservedItems);
            notificationService.sendOrderFailureAlert(customer.email(), "Shipping dispatch error.");
            return OrderResult.failure("Shipping error: " + ex.getMessage());
        }

        // 4. Send Confirmation
        notificationService.sendOrderConfirmation(customer.email(), orderId, trackingNumber);
        return OrderResult.ok(orderId, trackingNumber);
    }

    private void rollbackInventory(List<CartItem> reservedItems) {
        System.out.println("[OrderFacade] Triggering inventory compensation rollback...");
        for (CartItem item : reservedItems) {
            inventoryService.releaseStock(item.productId(), item.quantity());
        }
    }
}