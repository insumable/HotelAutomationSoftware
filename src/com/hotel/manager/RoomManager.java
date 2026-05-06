package com.hotel.manager;

import com.hotel.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class RoomManager {
    private final Database db = Database.get();

    public boolean addRoom(int roomNumber, BedType bedType, AmenityType amenityType,
                           double tariff, int floor) {
        if (db.getRooms().containsKey(roomNumber)) return false;
        db.getRooms().put(roomNumber, new Room(roomNumber, bedType, amenityType, tariff, floor));
        return true;
    }

    public boolean updateTariff(int roomNumber, double newTariff) {
        Room r = db.getRooms().get(roomNumber);
        if (r == null) return false;
        r.setTariffPerNight(newTariff);
        return true;
    }

    public boolean setStatus(int roomNumber, RoomStatus status) {
        Room r = db.getRooms().get(roomNumber);
        if (r == null) return false;
        r.setStatus(status);
        return true;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(db.getRooms().values());
    }

    public List<Room> getAvailableRooms(BedType bt, AmenityType at) {
        return db.getRooms().values().stream()
                .filter(r -> r.getBedType() == bt
                          && r.getAmenityType() == at
                          && r.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    public Room getRoom(int roomNumber) {
        return db.getRooms().get(roomNumber);
    }
}
