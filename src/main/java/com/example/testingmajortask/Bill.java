package com.example.testingmajortask;

public class Bill {
    private Ticket ticket;
    private String paymentType;

    public Bill(Ticket ticket, String paymentType) {
        this.ticket = ticket;
        this.paymentType = paymentType;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public static Bill generateBill(Ticket ticket, String paymentType) {
        return new Bill(ticket, paymentType);
    }
}