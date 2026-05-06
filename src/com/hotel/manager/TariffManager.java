package com.hotel.manager;

import com.hotel.model.*;
import java.time.*;
import java.util.*;

public class TariffManager {
    private final Database db = Database.get();

    /** P5.1 — compute average occupancy for a given month/year (0-100) */
    public double computeOccupancy(int month, int year) {
        long totalRooms = db.getRooms().size();
        if (totalRooms == 0) return 0;

        YearMonth ym = YearMonth.of(year, month);
        int days = ym.lengthOfMonth();
        double sum = 0;

        for (int day = 1; day <= days; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            long occupiedCount = db.getGuests().values().stream().filter(g -> {
                LocalDate arrival = g.getArrivalDateTime().toLocalDate();
                LocalDate departure = arrival.plusDays(g.getStayDurationNights());
                return !date.isBefore(arrival) && date.isBefore(departure);
            }).count();
            sum += (occupiedCount * 100.0) / totalRooms;
        }
        return Math.round((sum / days) * 100) / 100.0;
    }

    /** Apply % tariff revision to a room category (bedType + amenityType) */
    public int applyRevision(BedType bedType, AmenityType amenityType, double pct) {
        int count = 0;
        for (Room r : db.getRooms().values()) {
            if (r.getBedType() == bedType && r.getAmenityType() == amenityType) {
                double prev = r.getTariffPerNight();
                double revised = Math.round(prev * (1 + pct / 100) * 100) / 100.0;
                r.setTariffPerNight(revised);
                db.getTariffHistory().add(String.format(
                    "[%s] Room %d %s/%s: %.2f → %.2f (%.1f%%)",
                    LocalDate.now(), r.getRoomNumber(), bedType, amenityType, prev, revised, pct));
                count++;
            }
        }
        return count;
    }

    public List<String> getRevisionHistory() {
        return Collections.unmodifiableList(db.getTariffHistory());
    }
}
