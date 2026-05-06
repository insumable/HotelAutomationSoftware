package com.hotel.model;

public class FrequentGuest {
    private String freqGuestId;
    private String guestName;
    private String contactNumber;
    private double discountRate;   // 0-100
    private int totalVisits;

    public FrequentGuest(String freqGuestId, String guestName,
                         String contactNumber, double discountRate) {
        this.freqGuestId    = freqGuestId;
        this.guestName      = guestName;
        this.contactNumber  = contactNumber;
        this.discountRate   = discountRate;
        this.totalVisits    = 0;
    }

    public String getFreqGuestId()      { return freqGuestId; }
    public String getGuestName()        { return guestName; }
    public String getContactNumber()    { return contactNumber; }
    public double getDiscountRate()     { return discountRate; }
    public int    getTotalVisits()      { return totalVisits; }

    public void setDiscountRate(double r) { this.discountRate = r; }
    public void incrementVisits()         { this.totalVisits++; }

    @Override public String toString() {
        return String.format("[%s] %s | Discount: %.1f%% | Visits: %d",
                freqGuestId, guestName, discountRate, totalVisits);
    }
}
