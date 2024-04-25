package com.example.testingmajortask;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Flight {
    private String destination;
    private String date;
    private String departure_time;
    private String arrival_time;
    private int noSeats;
    private ArrayList<Ticket> available_tickets;
    private String flight_id;
    private boolean is_delayed;

    // prices of seats
    private int econonmyCPrice;
    private int firstCPrice;
    private int businessCPrice;
    private Bill bill;

    // retrieve a flight
    public Flight(String flight_id) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;

        while((line = file.readLine()) != null){
            String[] parts = line.split(";");
            if(Objects.equals(flight_id, parts[5])){
                this.destination = parts[0];
                this.date = parts[1];
                this.departure_time = parts[2];
                this.arrival_time = parts[3];
                this.noSeats = Integer.parseInt(parts[4]);
                this.flight_id = parts[5];
                this.is_delayed = Boolean.parseBoolean(parts[6]);
                this.econonmyCPrice = Integer.parseInt(parts[7]);
                this.firstCPrice = Integer.parseInt(parts[8]);
                this.businessCPrice = Integer.parseInt(parts[9]);
            }
        }
        file.close();
    }

    // create new flight
    public Flight(String destination, String date, String departure_time, String arrival_time, int noSeats, String flight_id, boolean is_delayed, int econonmyCPrice, int firstCPrice, int businessCPrice) throws IOException {
        available_tickets = new ArrayList<Ticket>();
        StringBuilder ticketID = new StringBuilder();

        // add flights
        BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Flights.txt", true));
        bufferedWriter1.write(destination+";"+date+";"+departure_time+";"+arrival_time+";"+noSeats+";"+flight_id+";"+is_delayed+";"+econonmyCPrice+";"+firstCPrice+";"+businessCPrice);
        bufferedWriter1.newLine();
        bufferedWriter1.close();

        // add tickets
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Tickets.txt", true));

        for (int i = 0; i < noSeats; i++) {
            ticketID = new StringBuilder();
            ticketID.append(flight_id).append("_T:").append(i);
            if (i < noSeats / 2) {
                // it is in economy
                bufferedWriter.write(new Ticket(flight_id, ticketID.toString(), "Economy Class", econonmyCPrice).getTicketDetails());
                bufferedWriter.newLine();
            } else {
                if (i <= (3 * noSeats) / 4) {
                    // it is first class
                    bufferedWriter.write(new Ticket(flight_id, ticketID.toString(), "First Class", firstCPrice).getTicketDetails());
                    bufferedWriter.newLine();
                } else {
                    // it is business class
                    bufferedWriter.write(new Ticket(flight_id, ticketID.toString(), "Business Class", businessCPrice).getTicketDetails());
                    bufferedWriter.newLine();
                }
            }
        }
        bufferedWriter.close();
    }

    // Getters and Setters
    public String getDeparture_time(){
        return this.departure_time;
    }
    public String getArrival_time(){
        return this.arrival_time;
    }
    public String getFlightDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Flight ID: ").append(flight_id).append("\n");
        details.append("Destination: ").append(destination).append("\n");
        details.append("Date: ").append(date).append("\n");
        details.append("Departure time: ").append(departure_time).append("\n");
        details.append("Arrival Time: ").append(arrival_time).append("\n");
        details.append("Is it delayed? ").append(is_delayed ? "Yes" : "No").append("\n");

        return details.toString();
    }

    public String getFlight_id(){
        return this.flight_id;
    }
    public boolean getStatus(){
        return this.is_delayed;
    }

    public String getDate(){
        return this.date;
    }

    public int getNoSeats(){
        return this.noSeats;
    }

    public String getDestination() {
        return destination;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int bookTicket(String ticketID) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt"));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = file.readLine()) != null) {
            String[] parts = line.split(";");
            if (Objects.equals(parts[0], "username")) {
                continue;
            }
            if (Objects.equals(ticketID, parts[3])) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/BookedTickets.txt", true));
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } else {
                sb.append(line).append("\n");
            }
        }
        file.close();

        PrintWriter writer = new PrintWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt"));
        writer.write(sb.toString());
        writer.close();
        return noSeats;
    }

    public int cancelTicket(String ticketID) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt"));
        String line;
        StringBuilder reservedTickets = new StringBuilder();

        Boolean foundTicket = false;

        Ticket t = null;

        while((line = file.readLine()) != null){
            String[] parts = line.split(";");
            if(Objects.equals(parts[0], User.name) && !foundTicket){
                if(Objects.equals(parts[3], ticketID)){
                    t = new Ticket(parts[2],parts[3],parts[4],Integer.parseInt(parts[5]));

                    foundTicket = true;
                    BufferedWriter file2 = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Tickets.txt", true));
                    file2.write(t.getTicketDetails());
                    file2.newLine();
                    file2.close();
                    continue;
                }
            }
            reservedTickets.append(line).append("\n");
        }
        file.close();

        PrintWriter writer = new PrintWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt"));
        writer.write(reservedTickets.toString());
        writer.close();
        return foundTicket ? addSeat(t.getFlight_id()) : -1; // ticket not found
    }
    public int reserveTicket(String t_class) throws IOException {
        if(removeSeat(flight_id) == -1){
            return -1; // invalid request
        }

        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Tickets.txt"));
        String line;
        StringBuilder sb = new StringBuilder();

        Boolean skip = false;

        while ((line = file.readLine()) != null) {
            String[] parts = line.split(";");
            if(Objects.equals(flight_id, parts[0])){
                if (Objects.equals(parts[2], t_class) && !skip) {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt", true));
                    bufferedWriter.write(User.name + ";" + (new Flight(line.split(";")[0])).getDestination() + ";" + line);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    skip = true;
                    continue;
                }
            }
            sb.append(line).append("\n");
        }
        file.close();

        PrintWriter writer = new PrintWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Tickets.txt"));
        writer.write(sb.toString());
        writer.close();

        return new Flight(flight_id).getNoSeats();
    }
    public static int removeSeat(String flight_id) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;
        StringBuilder sb = new StringBuilder();

        int newNoSeats = 0;

        while((line = file.readLine()) != null){
            String[] parts = line.split(";");

            if(Objects.equals(flight_id, parts[5])){
                if(Integer.parseInt(parts[4]) == 0){
                    return -1; // invalid request
                }
                else{
                    newNoSeats = Integer.parseInt(parts[4]) - 1;
                    parts[4] = (Integer.parseInt(parts[4]) - 1) + "";

                    // Reversing the array
                    StringBuilder reversedText = new StringBuilder();
                    for (int i = 0; i <= parts.length - 1; i++) {
                        reversedText.append(parts[i]);
                        if (i != parts.length - 1) {
                            reversedText.append(";");
                        }
                    }
                    line = reversedText.toString();
                }
            }
            sb.append(line).append("\n");
        }
        PrintWriter writer = new PrintWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        writer.write(sb.toString());
        writer.close();

        return newNoSeats;
    }
    public static int addSeat(String flight_id) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;
        StringBuilder sb = new StringBuilder();

        int newNoSeats = 0;

        while((line = file.readLine()) != null){
            String[] parts = line.split(";");

            if(Objects.equals(flight_id, parts[5])){
                newNoSeats = Integer.parseInt(parts[4]) + 1;
                parts[4] = (Integer.parseInt(parts[4]) + 1) + "";

                // Reversing the array
                StringBuilder reversedText = new StringBuilder();
                for (int i = 0; i <= parts.length - 1; i++) {
                    reversedText.append(parts[i]);
                    if (i != parts.length - 1) {
                        reversedText.append(";");
                    }
                }
                line = reversedText.toString();
            }
            sb.append(line).append("\n");
        }
        PrintWriter writer = new PrintWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        writer.write(sb.toString());
        writer.close();

        return newNoSeats;
    }
    public static boolean updateDelayed(String flight_id, boolean is_delayed) throws IOException{

        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;
        StringBuilder sb = new StringBuilder();

        String lineToReturn = "";

        while((line = file.readLine()) != null){
            String[] parts = line.split(";");

            if(Objects.equals(flight_id, parts[5])){
                parts[6] = is_delayed + "";
                // Reversing the array
                StringBuilder reversedText = new StringBuilder();
                for (int i = 0; i <= parts.length - 1; i++) {
                    reversedText.append(parts[i]);
                    if (i != parts.length - 1) {
                        reversedText.append(";");
                    }
                }
                line = reversedText.toString();
                lineToReturn = line;
            }
            sb.append(line).append("\n");
        }
        BufferedWriter file2 = new BufferedWriter(new FileWriter("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        file2.write(sb.toString());
        file2.close();
        return Boolean.parseBoolean(lineToReturn.split(";")[6]);
    }

    public static ArrayList<Flight> displayAllFlights() throws IOException{
        // returns the size of the array list of all the flights stored
        // returns null if no flights exist in file

        ArrayList<Flight> flights = new ArrayList<Flight>();
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;

        while((line = file.readLine()) != null){
            flights.add(new Flight(line.split(";")[5]));
        }

        file.close();
        return flights.isEmpty() ? null : flights;
    }

    public static ArrayList<Flight> searchFlight(String destination) throws IOException{
        // function to return a list of flights that go to the desired searched destination
        // returns null if no flights exist in file with the desired destination

        ArrayList<Flight> flights = new ArrayList<Flight>();
        BufferedReader file = new BufferedReader(new FileReader("./src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        String line;

        while((line = file.readLine()) != null){
            if(Objects.equals(destination, line.split(";")[0])){
                flights.add(new Flight(line.split(";")[5]));
            }
        }

        return flights.isEmpty() ? null : flights;
    }
}