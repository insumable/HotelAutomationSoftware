package com.hotel.manager;

import com.hotel.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class CateringManager {
    private final Database db = Database.get();

    /** Validates token belongs to active guest then saves order */
    public CateringOrder addFoodOrder(String token, String item, int qty, double price) {
        Guest g = db.getGuests().get(token);
        if (g == null || g.isCheckedOut()) return null;
        CateringOrder order = new CateringOrder(token, item, qty, price);
        db.getCateringOrders().add(order);
        return order;
    }

    public List<CateringOrder> getOrdersByToken(String token) {
        return db.getCateringOrders().stream()
                .filter(o -> o.getTokenNumber().equals(token))
                .collect(Collectors.toList());
    }

    public double getTotalByToken(String token) {
        return getOrdersByToken(token).stream()
                .mapToDouble(CateringOrder::getLineTotal).sum();
    }
}
