package com.example.testingmajortask;

public class Ticket {
    private String flight_id;
    private String ticket_id;
    private String seatType;
    private int price;

    // Constructor
    public Ticket(String flight_id, String ticket_id, String seatType, int price) {
        this.flight_id = flight_id;
        this.ticket_id = ticket_id;
        this.seatType = seatType;
        this.price = price;
    }
    public String getTicketDetails() {
        StringBuilder details = new StringBuilder();
        details.append(flight_id).append(";");
        details.append(ticket_id).append(";");
        details.append(seatType).append(";");
        details.append(price);

        return details.toString();
    }
    // Getters and Setters
    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}