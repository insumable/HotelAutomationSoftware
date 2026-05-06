package com.hotel.manager;

import com.hotel.model.*;
import java.util.*;

/**
 * D1-D6 in-memory data stores as described in the SA/SD document.
 */
public class Database {
    private static final Database INSTANCE = new Database();

    // D1: Room
    private final Map<Integer, Room> rooms = new LinkedHashMap<>();
    // D2: Guest (token → Guest)
    private final Map<String, Guest> guests = new LinkedHashMap<>();
    // D3: CateringOrder (orderId → order)
    private final List<CateringOrder> cateringOrders = new ArrayList<>();
    // D4: OccupancyLog (simple status snapshots — omitted for brevity; occupancy computed from guests)
    // D5: FrequentGuest
    private final Map<String, FrequentGuest> freqGuests = new LinkedHashMap<>();
    // D6: TariffHistory
    private final List<String> tariffHistory = new ArrayList<>();
    // Users
    private final Map<String, User> users = new LinkedHashMap<>();

    private Database() { seed(); }

    public static Database get() { return INSTANCE; }

    // ── accessors ──────────────────────────────────────────────────────────
    public Map<Integer, Room>      getRooms()          { return rooms; }
    public Map<String, Guest>      getGuests()         { return guests; }
    public List<CateringOrder>     getCateringOrders() { return cateringOrders; }
    public Map<String, FrequentGuest> getFreqGuests()  { return freqGuests; }
    public List<String>            getTariffHistory()  { return tariffHistory; }
    public Map<String, User>       getUsers()          { return users; }

    // ── seed data ──────────────────────────────────────────────────────────
    private void seed() {
        // Rooms
        rooms.put(101, new Room(101, BedType.SINGLE, AmenityType.AC,   2500, 1));
        rooms.put(102, new Room(102, BedType.SINGLE, AmenityType.NON_AC, 1800, 1));
        rooms.put(103, new Room(103, BedType.DOUBLE, AmenityType.AC,   4000, 1));
        rooms.put(104, new Room(104, BedType.DOUBLE, AmenityType.NON_AC, 3000, 1));
        rooms.put(201, new Room(201, BedType.SINGLE, AmenityType.AC,   2700, 2));
        rooms.put(202, new Room(202, BedType.DOUBLE, AmenityType.AC,   4500, 2));
        rooms.put(203, new Room(203, BedType.DOUBLE, AmenityType.NON_AC, 3200, 2));
        rooms.put(301, new Room(301, BedType.SINGLE, AmenityType.AC,   3000, 3));
        rooms.put(302, new Room(302, BedType.DOUBLE, AmenityType.AC,   5000, 3));
        rooms.put(303, new Room(303, BedType.DOUBLE, AmenityType.NON_AC, 3500, 3));

        // Frequent guests
        freqGuests.put("FG001", new FrequentGuest("FG001", "Rajesh Kumar",  "9876543210", 10.0));
        freqGuests.put("FG002", new FrequentGuest("FG002", "Priya Sharma",  "9123456780", 15.0));

        // Default users
        users.put("admin",     new User("admin",     "admin123",     UserRole.SYSTEM_ADMIN));
        users.put("reception", new User("reception", "rec123",       UserRole.RECEPTIONIST));
        users.put("catering",  new User("catering",  "cat123",       UserRole.CATERING_MANAGER));
        users.put("manager",   new User("manager",   "mgr123",       UserRole.HOTEL_MANAGER));
    }
}
