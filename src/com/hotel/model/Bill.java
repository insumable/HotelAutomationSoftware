package com.hotel.model;

public class Bill {
    private static int counter = 1;

    private int    billId;
    private String tokenNumber;
    private double roomCharge;
    private double cateringTotal;
    private double discountAmount;
    private double taxAmount;
    private double advancePaid;
    private double netPayable;

    public Bill(String tokenNumber, double roomCharge, double cateringTotal,
                double discountAmount, double taxAmount, double advancePaid) {
        this.billId        = counter++;
        this.tokenNumber   = tokenNumber;
        this.roomCharge    = roomCharge;
        this.cateringTotal = cateringTotal;
        this.discountAmount= discountAmount;
        this.taxAmount     = taxAmount;
        this.advancePaid   = advancePaid;
        this.netPayable    = Math.round((roomCharge + cateringTotal
                            - discountAmount + taxAmount - advancePaid) * 100.0) / 100.0;
    }

    public int    getBillId()         { return billId; }
    public String getTokenNumber()    { return tokenNumber; }
    public double getRoomCharge()     { return roomCharge; }
    public double getCateringTotal()  { return cateringTotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTaxAmount()      { return taxAmount; }
    public double getAdvancePaid()    { return advancePaid; }
    public double getNetPayable()     { return netPayable; }

    public String getSummary() {
        return String.format(
            "Bill #%d | Token: %s\n" +
            "Room Charges    : ₹ %,.2f\n" +
            "Catering Total  : ₹ %,.2f\n" +
            "Gross Total     : ₹ %,.2f\n" +
            "Discount        : ₹ %,.2f\n" +
            "GST (12%%)       : ₹ %,.2f\n" +
            "Advance Paid    : ₹ %,.2f\n" +
            "─────────────────────────\n" +
            "NET PAYABLE     : ₹ %,.2f",
            billId, tokenNumber,
            roomCharge, cateringTotal, roomCharge + cateringTotal,
            discountAmount, taxAmount, advancePaid, netPayable);
    }
}
