package com.hotel.manager;

import com.hotel.model.*;

public class BillingManager {
    private final Database        db  = Database.get();
    private final CateringManager cm  = new CateringManager();

    /** P4.1-P4.5: generates itemised Bill; sets guest checked-out & room available */
    public Bill generateBill(String token) {
        Guest g = db.getGuests().get(token);
        if (g == null || g.isCheckedOut()) return null;

        Room room = db.getRooms().get(g.getAssignedRoomNumber());
        if (room == null) return null;

        // P4.1
        int nights = Math.max(1, g.getStayDurationNights());
        double roomCharge = room.getTariffPerNight() * nights;

        // P4.2
        double cateringTotal = cm.getTotalByToken(token);

        double grossTotal = roomCharge + cateringTotal;

        // P4.3
        double discountRate = 0;
        String fgId = g.getFreqGuestId();
        if (fgId != null && db.getFreqGuests().containsKey(fgId))
            discountRate = db.getFreqGuests().get(fgId).getDiscountRate();
        double discountAmount = Math.round(grossTotal * (discountRate / 100) * 100) / 100.0;

        // P4.4
        double taxBase   = grossTotal - discountAmount;
        double taxAmount = Math.round(taxBase * 0.12 * 100) / 100.0;

        Bill bill = new Bill(token, roomCharge, cateringTotal,
                             discountAmount, taxAmount, g.getAdvancePaid());

        // update states
        g.setCheckedOut(true);
        room.setStatus(RoomStatus.AVAILABLE);

        return bill;
    }

    public FreqGuestManager getFreqGuestManager() { return new FreqGuestManager(); }
}
