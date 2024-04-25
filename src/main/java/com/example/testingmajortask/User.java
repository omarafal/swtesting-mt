package com.example.testingmajortask;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    public static String name;
    public static String address;
    public static String number;
    public static Boolean isAdmin;

    User(String name, int id, String password, String address, String number, Boolean isAdmin) throws IOException {
        // LOGIC FOR ADDING A NEW USER TO FILE
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./src/Users.txt", true));
        bufferedWriter.write(name + ":" + id + ":" + password + ":" + address+ ":" + number + ":" + isAdmin);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    static String getPassengerInformation(){
        StringBuilder details = new StringBuilder();
        details.append("Name: " + name).append("\n");
        details.append("Address: " + address).append("\n");
        details.append("Number: " + number).append("\n");
        details.append("isAdmin: " + isAdmin).append("\n");

        return details.toString();
    }

    public static ArrayList<Ticket> getBookedTickets() throws IOException {
        ArrayList<Ticket> booked_tickets = new ArrayList<Ticket>();

        // access file
        String path = "./src/BookedTickets.txt";
        String line;
        BufferedReader file = new BufferedReader(new FileReader(path));
        while ((line = file.readLine()) != null) {
            // skip first line
            if(Objects.equals(line.split(";")[0], "flight_id")){
                continue;
            }
            String[] parts = line.split(";");
            if(Objects.equals(parts[0], User.name)){
                booked_tickets.add(new Ticket(parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
            }
        }
        return booked_tickets;
    }

    public static int calculateFares() throws IOException {
        ArrayList<Ticket> booked_tickets = getBookedTickets();
        int sum = 0;
        for(Ticket t: booked_tickets){
            sum += t.getPrice();
        }
        return sum;
    }

    public static int loginUser(String name, String password) throws IOException {
        /*
         * sets user's name and details, returns -1 if user not found
         * */

        String path = "./src/main/java/com/example/testingmajortask/Database/Users.txt";
        String line;
        BufferedReader file = new BufferedReader(new FileReader(path));
        while ((line = file.readLine()) != null) {
            // skip first line
            if(Objects.equals(line.split(";")[0], "username")){
                continue;
            }
            String[] parts = line.split(";");
            if(Objects.equals(parts[0], name)){
                if(Objects.equals(password, parts[2])){
                    // User validated
                    User.name = parts[0];
                    User.address = parts[3];
                    User.number = parts[4];
                    User.isAdmin = Boolean.valueOf(parts[5]);

                    return Integer.parseInt(parts[1]); // return id
                }
            }
        }
        file.close();
        return -1;
    }
}
