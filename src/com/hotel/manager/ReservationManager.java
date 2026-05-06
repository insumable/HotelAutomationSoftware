package com.hotel.manager;

import com.hotel.model.*;
import java.time.LocalDateTime;
import java.util.*;

public class ReservationManager {
    private final Database    db   = Database.get();
    private final RoomManager rm   = new RoomManager();

    /** P2.1 Validate + P2.2 Assign + P2.3 Register → returns token or null */
    public Guest makeReservation(String guestName, String contact, String idProof,
                                 LocalDateTime arrival, int nights, double advance,
                                 BedType bedType, AmenityType amenity, String freqGuestId) {
        List<Room> candidates = rm.getAvailableRooms(bedType, amenity);
        if (candidates.isEmpty()) return null;                         // P2.4 apology

        Room chosen = candidates.get(0);
        chosen.setStatus(RoomStatus.OCCUPIED);

        String token = generateToken();
        Guest g = new Guest(token, guestName, contact, idProof,
                            arrival, nights, advance,
                            chosen.getRoomNumber(), freqGuestId);
        db.getGuests().put(token, g);

        // increment freq-guest visits
        if (freqGuestId != null && db.getFreqGuests().containsKey(freqGuestId))
            db.getFreqGuests().get(freqGuestId).incrementVisits();

        return g;
    }

    public Guest getGuestByToken(String token) {
        return db.getGuests().get(token);
    }

    public List<Guest> getActiveGuests() {
        List<Guest> list = new ArrayList<>();
        for (Guest g : db.getGuests().values())
            if (!g.isCheckedOut()) list.add(g);
        return list;
    }

    private String generateToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        String tok = sb.toString();
        return db.getGuests().containsKey(tok) ? generateToken() : tok;
    }
}
