package com.ecommerce.subsystem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {
    private final Map<String, Integer> stockDatabase = new ConcurrentHashMap<>();

    public InventoryService() {
        stockDatabase.put("PROD-101", 10);
        stockDatabase.put("PROD-102", 2);
    }

    public synchronized boolean reserveStock(String productId, int quantity) {
        int available = stockDatabase.getOrDefault(productId, 0);
        if (available >= quantity) {
            stockDatabase.put(productId, available - quantity);
            System.out.printf("[InventoryService] Reserved %d units of %s. In stock: %d%n",
                    quantity, productId, stockDatabase.get(productId));
            return true;
        }
        System.err.printf("[InventoryService] Insufficient stock for %s. Requested: %d, Available: %d%n",
                productId, quantity, available);
        return false;
    }

    public synchronized void releaseStock(String productId, int quantity) {
        int updated = stockDatabase.getOrDefault(productId, 0) + quantity;
        stockDatabase.put(productId, updated);
        System.out.printf("[InventoryService] Restored %d units of %s. In stock: %d%n",
                quantity, productId, updated);
    }
}