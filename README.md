# Assignment #4 — Facade Design Pattern

**Course:** ShP-2216 – Software Design Patterns  
**Language:** Java 17  
**Pattern:** Facade (Structural)

## Overview
This project implements the Facade Design Pattern to simplify an e-commerce checkout workflow. The `OrderFacade` coordinates four independent subsystems:
- `InventoryService`: Reserves and releases stock holds.
- `PaymentProcessor`: Charges customer payment tokens and handles refunds.
- `ShippingService`: Schedules parcel dispatch and creates tracking numbers.
- `NotificationService`: Dispatches order confirmation and failure emails.

## Subsystem Independence
Subsystem classes maintain zero references back to `OrderFacade`. Advanced clients or administrative cron jobs can bypass the facade to interact directly with any subsystem service.

## Failure Handling & Compensation
If any phase of the transaction fails (such as an inventory shortage, payment decline, or logistical routing error), `OrderFacade` initiates compensating rollback actions (e.g., restoring reserved stock or issuing payment refunds) before returning a clean `OrderResult`.

## Running the Application
```bash
javac -d out $(find src -name "*.java")
java -cp out com.ecommerce.Main