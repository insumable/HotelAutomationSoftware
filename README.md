HEAD
# 🏨 Hotel Automation Software

> A Java Swing desktop application built using Structured Analysis & Structured Design (SA/SD) methodology.  

---

## 📌 Overview

The Hotel Automation Software automates the day-to-day operations of a 5-star hotel — including room management, guest reservations, catering, billing, occupancy analytics, and loyalty programme management — through a role-based desktop GUI backed by an in-memory centralised database.

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher installed
- Any OS (Windows / macOS / Linux)

### Running the Application
```bash
java -jar HotelAutomation.jar
```

### Demo Login Credentials

| Username     | Password   | Role               | Access                              |
|--------------|------------|--------------------|-------------------------------------|
| `admin`      | `admin123` | System Admin       | All panels                          |
| `reception`  | `rec123`   | Receptionist       | Rooms, Reservations, Billing        |
| `catering`   | `cat123`   | Catering Manager   | Catering orders only                |
| `manager`    | `mgr123`   | Hotel Manager      | Occupancy & Tariff reports          |

---

## 🗂 Project Structure

```
HotelAutomation_src/
├── HotelApp.java                        ← Main entry point
└── com/hotel/
    ├── model/                           ← Entity classes (D1–D6 data stores)
    │   ├── BedType.java                 ← Enum: SINGLE | DOUBLE
    │   ├── AmenityType.java             ← Enum: AC | NON_AC
    │   ├── RoomStatus.java              ← Enum: AVAILABLE | OCCUPIED | MAINTENANCE
    │   ├── UserRole.java                ← Enum: RECEPTIONIST | CATERING_MANAGER | ...
    │   ├── Room.java                    ← Room number, bed type, tariff, floor, status
    │   ├── Guest.java                   ← Token, name, contact, arrival, nights, advance
    │   ├── CateringOrder.java           ← Food item, quantity, unit price, timestamp
    │   ├── Bill.java                    ← Room charge, catering, GST, discount, net payable
    │   ├── FrequentGuest.java           ← Loyalty ID, discount rate, visit count
    │   └── User.java                    ← Username, password, role
    ├── manager/                         ← Business logic modules
    │   ├── Database.java                ← Central singleton (all 6 data stores)
    │   ├── RoomManager.java             ← Add/edit rooms, search by availability
    │   ├── ReservationManager.java      ← Full booking flow with token generation
    │   ├── CateringManager.java         ← Log and retrieve food orders per token
    │   ├── BillingManager.java          ← P4 billing formula: GST + discount + advance
    │   ├── TariffManager.java           ← Monthly occupancy (P5.1) + tariff revision
    │   └── FreqGuestManager.java        ← Loyalty guest registration and lookup
    └── ui/                              ← Java Swing screens
        ├── UIConstants.java             ← Shared colors, fonts, button factory
        ├── LoginFrame.java              ← Login screen
        ├── MainFrame.java               ← Tabbed main window (role-aware)
        ├── RoomPanel.java               ← Room catalogue, add room, update status
        ├── ReservationPanel.java        ← New booking, auto room assignment, guest list
        ├── CateringPanel.java           ← Log food orders, filter by token, totals
        ├── BillingPanel.java            ← Generate itemised bill, checkout guest
        ├── TariffPanel.java             ← Occupancy report, percentage tariff revision
        ├── FreqGuestPanel.java          ← Loyalty programme management
        └── UserPanel.java               ← Admin: create/delete user accounts
```

---

## ⚙️ Features

### 🛏 Room Management
- Add rooms with room number, bed type (Single/Double), amenity (AC/Non-AC), tariff, and floor
- View the full room catalogue with live status
- Manually update room status: Available → Occupied → Maintenance

### 🛎 Reservation & Check-In
- Book a guest by entering name, contact, ID proof, stay duration, and room preference
- System automatically finds the first matching available room (P2.1–P2.2)
- Generates a unique 8-character alphanumeric **Token Number** as the session key
- Displays an apology message if no room is available (P2.4)
- Optional Frequent Guest ID linkage at check-in

### 🍽 Catering Services
- Log food orders (item, quantity, unit price) against an active guest token
- View and filter all orders by token
- Running catering total displayed per token

### 💳 Billing & Checkout
- Generates a fully itemised bill for any active guest:

```
Room Charges    = Tariff/Night × Nights
Catering Total  = Σ (Qty × Unit Price)
Gross Total     = Room + Catering
Discount        = Gross × (Discount Rate / 100)   ← if Frequent Guest
Tax (GST 12%)   = (Gross − Discount) × 0.12
Net Payable     = Gross − Discount + GST − Advance Paid
```

- Automatically marks guest as checked-out and sets room back to Available

### 📊 Occupancy & Tariff Management
- Compute average monthly occupancy percentage (P5.1) for any month/year
- Apply percentage-based tariff revision to any room category (bed type + amenity)
- All revisions are logged with date and room details

### ⭐ Frequent Guest Programme
- Register loyalty guests with a unique ID and configurable discount rate
- Discount is auto-applied at billing when a Frequent Guest ID is linked to the reservation
- Track total visits per loyalty member

### 👥 User Management *(System Admin only)*
- Create new user accounts with assigned roles
- Delete existing accounts (admin account is protected)

---

## 🏗 Architecture

Built following **SA/SD (Structured Analysis / Structured Design)** methodology:

| Layer | SA/SD Concept | Implementation |
|---|---|---|
| Data Stores (D1–D6) | DFD data stores | `Database.java` singleton with in-memory Maps/Lists |
| Processes (P1–P6) | DFD Level 1 processes | Manager classes (`RoomManager`, `BillingManager`, etc.) |
| Sub-processes | DFD Level 2 / mini-specs | Methods inside each Manager |
| UI | Interface layer | Java Swing panels per role |

### Data Stores

| ID | Store | Key |
|----|-------|-----|
| D1 | Room | `roomNumber` |
| D2 | Guest | `tokenNumber` |
| D3 | CateringOrder | `orderId` (auto) |
| D4 | OccupancyLog | computed from D2 |
| D5 | FrequentGuest | `freqGuestId` |
| D6 | TariffHistory | logged strings |

### Cohesion & Coupling

| Module | Cohesion | Coupling | Rating |
|---|---|---|---|
| ReservationManager | Functional | Data | ✅ Excellent |
| BillingManager | Functional | Data | ✅ Excellent |
| CateringManager | Functional | Data | ✅ Excellent |
| TariffManager | Communicational | Data | ✅ Acceptable |
| RoomManager | Functional | Data | ✅ Excellent |
| FreqGuestManager | Functional | Data | ✅ Excellent |

---

## 🧪 How to Compile from Source

```bash
# Unzip source
unzip HotelAutomation_src.zip

# Compile
mkdir out
find src -name "*.java" | xargs javac -d out

# Package into JAR
echo "Main-Class: HotelApp" > manifest.txt
jar cfm HotelAutomation.jar manifest.txt -C out .

# Run
java -jar HotelAutomation.jar
```

---

## 📋 UML Correspondence

| UML Diagram | Implemented As |
|---|---|
| Use Case Diagram | Role-based tab visibility in `MainFrame.java` |
| Class Diagram | All files under `com.hotel.model` and `com.hotel.manager` |
| Sequence Diagram (Reservation) | `ReservationManager.makeReservation()` flow |
| State-chart (Room) | `RoomStatus` enum + status transitions in managers |

---

## 📝 Notes

- All data is **in-memory only** — data resets when the application is closed. A future version can integrate JDBC with MySQL or SQLite for persistence.
- The application uses the **system look-and-feel** for native OS appearance.
- Designed for Java 11+; no external libraries required beyond the standard JDK.
=======
