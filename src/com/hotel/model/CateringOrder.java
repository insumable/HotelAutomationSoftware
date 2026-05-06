package com.hotel.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CateringOrder {
    private static int counter = 1;

    private int orderId;
    private String tokenNumber;
    private String foodItemName;
    private int quantity;
    private double unitPrice;
    private LocalDateTime orderTimestamp;

    public CateringOrder(String tokenNumber, String foodItemName,
                         int quantity, double unitPrice) {
        this.orderId        = counter++;
        this.tokenNumber    = tokenNumber;
        this.foodItemName   = foodItemName;
        this.quantity       = quantity;
        this.unitPrice      = unitPrice;
        this.orderTimestamp = LocalDateTime.now();
    }

    public int           getOrderId()        { return orderId; }
    public String        getTokenNumber()    { return tokenNumber; }
    public String        getFoodItemName()   { return foodItemName; }
    public int           getQuantity()       { return quantity; }
    public double        getUnitPrice()      { return unitPrice; }
    public LocalDateTime getOrderTimestamp() { return orderTimestamp; }
    public double        getLineTotal()      { return quantity * unitPrice; }

    @Override public String toString() {
        return String.format("#%d %s x%d @ %.2f = %.2f [%s]",
                orderId, foodItemName, quantity, unitPrice, getLineTotal(),
                orderTimestamp.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
