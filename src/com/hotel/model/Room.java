package com.hotel.model;

public class Room {
    private int roomNumber;
    private BedType bedType;
    private AmenityType amenityType;
    private double tariffPerNight;
    private RoomStatus status;
    private int floorNumber;

    public Room(int roomNumber, BedType bedType, AmenityType amenityType,
                double tariffPerNight, int floorNumber) {
        this.roomNumber = roomNumber;
        this.bedType = bedType;
        this.amenityType = amenityType;
        this.tariffPerNight = tariffPerNight;
        this.status = RoomStatus.AVAILABLE;
        this.floorNumber = floorNumber;
    }

    public int getRoomNumber()           { return roomNumber; }
    public BedType getBedType()          { return bedType; }
    public AmenityType getAmenityType()  { return amenityType; }
    public double getTariffPerNight()    { return tariffPerNight; }
    public RoomStatus getStatus()        { return status; }
    public int getFloorNumber()          { return floorNumber; }

    public void setStatus(RoomStatus s)      { this.status = s; }
    public void setTariffPerNight(double t)  { this.tariffPerNight = t; }

    @Override public String toString() {
        return String.format("Room %d [%s/%s] Floor:%d Tariff:%.2f Status:%s",
                roomNumber, bedType, amenityType, floorNumber, tariffPerNight, status);
    }
}
