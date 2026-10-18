package com.ecommerce;

import com.ecommerce.facade.OrderFacade;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.OrderResult;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        OrderFacade facade = new OrderFacade();
        Customer customer = new Customer("C-100", "Alikhan Nurpeis", "alikhan@example.kz", "Dostyk Ave 42, Almaty");

        System.out.println("=== SCENARIO 1: Successful Order ===");
        List<CartItem> cart1 = List.of(
                new CartItem("PROD-101", "Mechanical Keyboard", 1, 120.0),
                new CartItem("PROD-102", "Ergonomic Mouse", 1, 60.0)
        );
        OrderResult r1 = facade.placeOrder(customer, cart1, "VALID_TOKEN_123");
        System.out.println("Outcome: " + r1 + "\n");

        System.out.println("=== SCENARIO 2: Out of Stock Failure ===");
        List<CartItem> cart2 = List.of(
                new CartItem("PROD-102", "Ergonomic Mouse", 5, 60.0) // Only 1 unit remaining
        );
        OrderResult r2 = facade.placeOrder(customer, cart2, "VALID_TOKEN_123");
        System.out.println("Outcome: " + r2 + "\n");

        System.out.println("=== SCENARIO 3: Payment Declined with Rollback ===");
        List<CartItem> cart3 = List.of(
                new CartItem("PROD-101", "Mechanical Keyboard", 1, 120.0)
        );
        OrderResult r3 = facade.placeOrder(customer, cart3, "EXPIRED_TOKEN");
        System.out.println("Outcome: " + r3 + "\n");
    }
}