package com.hotel.manager;

import com.hotel.model.*;
import java.util.*;

public class FreqGuestManager {
    private final Database db = Database.get();

    public boolean register(String id, String name, String contact, double rate) {
        if (db.getFreqGuests().containsKey(id)) return false;
        db.getFreqGuests().put(id, new FrequentGuest(id, name, contact, rate));
        return true;
    }

    public FrequentGuest lookupById(String id) {
        return db.getFreqGuests().get(id);
    }

    public double getDiscountRate(String id) {
        FrequentGuest fg = db.getFreqGuests().get(id);
        return fg != null ? fg.getDiscountRate() : 0.0;
    }

    public List<FrequentGuest> getAllProfiles() {
        return new ArrayList<>(db.getFreqGuests().values());
    }

    public boolean updateDiscount(String id, double rate) {
        FrequentGuest fg = db.getFreqGuests().get(id);
        if (fg == null) return false;
        fg.setDiscountRate(rate);
        return true;
    }
}
