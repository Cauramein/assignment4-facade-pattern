package com.ecommerce.model;

public record CartItem(String productId, String name, int quantity, double unitPrice) {

}
