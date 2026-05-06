package com.hotel.model;

import java.time.LocalDateTime;

public class Guest {
    private String tokenNumber;
    private String guestName;
    private String contactNumber;
    private String idProof;
    private LocalDateTime arrivalDateTime;
    private int stayDurationNights;
    private double advancePaid;
    private int assignedRoomNumber;
    private String freqGuestId;          // nullable
    private boolean checkedOut;

    public Guest(String tokenNumber, String guestName, String contactNumber,
                 String idProof, LocalDateTime arrivalDateTime,
                 int stayDurationNights, double advancePaid,
                 int assignedRoomNumber, String freqGuestId) {
        this.tokenNumber        = tokenNumber;
        this.guestName          = guestName;
        this.contactNumber      = contactNumber;
        this.idProof            = idProof;
        this.arrivalDateTime    = arrivalDateTime;
        this.stayDurationNights = stayDurationNights;
        this.advancePaid        = advancePaid;
        this.assignedRoomNumber = assignedRoomNumber;
        this.freqGuestId        = freqGuestId;
        this.checkedOut         = false;
    }

    public String        getTokenNumber()         { return tokenNumber; }
    public String        getGuestName()           { return guestName; }
    public String        getContactNumber()       { return contactNumber; }
    public String        getIdProof()             { return idProof; }
    public LocalDateTime getArrivalDateTime()     { return arrivalDateTime; }
    public int           getStayDurationNights()  { return stayDurationNights; }
    public double        getAdvancePaid()         { return advancePaid; }
    public int           getAssignedRoomNumber()  { return assignedRoomNumber; }
    public String        getFreqGuestId()         { return freqGuestId; }
    public boolean       isCheckedOut()           { return checkedOut; }

    public void setCheckedOut(boolean b) { this.checkedOut = b; }

    @Override public String toString() {
        return String.format("[%s] %s | Room:%d | Nights:%d",
                tokenNumber, guestName, assignedRoomNumber, stayDurationNights);
    }
}
