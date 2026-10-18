package com.ecommerce.model;

import java.util.List;

public record Order(String orderId, Customer customer, List<CartItem> items, double totalAmount) {}