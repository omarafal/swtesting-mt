package com.example.testingmajortask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {
    @FXML
    private Label totalFareLabel;

    @FXML
    private Label ticket_ticketID;
    @FXML
    private Label ticket_destination;
    @FXML
    private Label ticket_reserver;
    @FXML
    private Label ticket_flightDate;
    @FXML
    private Label ticket_flightStatus;
    @FXML
    private Label ticket_flightID;
    @FXML
    private Label ticket_flightDeparture;
    @FXML
    private Label ticket_flightArrival;

    @FXML
    public Label username;

    @FXML
    Button adminFlightsBtn;

    @FXML
    TextField searchBar;
    @FXML
    ListView<HBoxCell> flightsList;
    @FXML
    ListView<HBoxCell> reservedList;
    @FXML
    ListView<HBoxCell> bookedList;
    @FXML
    ListView<HBoxCell> adminFlightsList;

    // panes
    @FXML
    VBox defaultPane;
    @FXML
    VBox flightsPane;
    @FXML
    VBox reservedPane;
    @FXML
    VBox bookedPane;
    @FXML
    VBox sidePane;
    @FXML
    VBox printPane;
    @FXML
    AnchorPane ticketPane;
    @FXML
    AnchorPane newFlightPane;
    @FXML
    VBox adminFlightsPane;

    // New flight pane text fields
    @FXML
    TextField destinationField;
    @FXML
    TextField departureField;
    @FXML
    TextField numberSeatsField;
    @FXML
    TextField arrivalTimeField;
    @FXML
    TextField flightIDField;
    @FXML
    TextField ePriceField;
    @FXML
    TextField fPriceField;
    @FXML
    TextField bPriceField;
    @FXML
    DatePicker flightDateField;

    public void initialize() {
        username.setText(User.name);
        if(User.isAdmin){
            adminFlightsBtn.setVisible(true);
        }
    }

    @FXML
    protected void onAdminFlightsBtnClicked() throws IOException {
        switchPane("adminFlights");

        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>(); // list.add(new HBoxCell())
        ArrayList<Flight> flights = Flight.displayAllFlights();

        for(Flight flight : flights){
            flightsArrFile.add(new HBoxCell(flight.getDestination(), flight.getFlight_id()));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        adminFlightsList.setItems(myObservableList);
    }

    @FXML
    protected void onAddNewFlightBtnClicked(){
        switchPane("newFlight");
    }

    @FXML
    protected void onSubmitFlightDataBtnClicked() throws IOException {
        String pattern = "dd/MM/yyyy";
        LocalDate selectedDate = flightDateField.getValue();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = dateFormatter.format(selectedDate);
        new Flight(destinationField.getText(), formattedDate, departureField.getText(), arrivalTimeField.getText(), Integer.parseInt(numberSeatsField.getText()), flightIDField.getText(), false, Integer.parseInt(ePriceField.getText()), Integer.parseInt(fPriceField.getText()), Integer.parseInt(bPriceField.getText()));
        onReturnBtnClicked();
    }

    @FXML
    protected void onSearchBtnClicked() throws IOException {
        String text = searchBar.getText(); // text in search bar field
        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>();

        if(text.isEmpty()){
            onFlightsBtnClicked();
            return;
        }

        ArrayList<Flight> searchedFlights = Flight.searchFlight(text); // takes destination
        if(searchedFlights == null){
            String noFlights = "Our apologies! No flights go to your requested destination.";
            flightsArrFile.add(new HBoxCell(noFlights));
        }

        else{
            for(Flight flight : searchedFlights){
                flightsArrFile.add(new HBoxCell(flight.getDestination(), flight.getDate(), flight.getNoSeats()+"", flight.getFlight_id()));
            }
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        flightsList.setItems(myObservableList);

    }

    // functions that switch main menu panes
    @FXML
    protected void onReservedBtnClicked() throws IOException {
        int total_fare = 0;

        switchPane("reserved");
        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>();
        String line;
        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/ReservedTickets.txt"));
        while ((line = file.readLine()) != null) {
            String[] parts = line.split(";");

            if(Objects.equals(parts[0], User.name)){
                total_fare += Integer.parseInt(parts[5]);

                flightsArrFile.add(new HBoxCell(parts[1], parts[2], parts[4], Integer.parseInt(parts[5]), parts[3]));
            }
        }
        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        reservedList.setItems(myObservableList);
        totalFareLabel.setText("$" + total_fare);
    }

    @FXML
    protected void onBookedBtnClicked() throws IOException{
        switchPane("booked");
        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>();
        String line;

        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/BookedTickets.txt"));
        while ((line = file.readLine()) != null) {
            String[] parts = line.split(";");
            if(Objects.equals(parts[0], User.name)){
                flightsArrFile.add(new HBoxCell(parts[1], parts[3], parts[4]));
            }
        }
        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        bookedList.setItems(myObservableList);
    }

    @FXML
    protected void onReturnBtnClicked() throws IOException {
        sidePane.setVisible(true);
        onFlightsBtnClicked();
        ticketPane.setVisible(false);
        newFlightPane.setVisible(false);
    }

    @FXML
    protected void onFlightsBtnClicked() throws IOException {
        switchPane("flights");

        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>(); // list.add(new HBoxCell())
        ArrayList<Flight> flights = Flight.displayAllFlights();

        for(Flight flight : flights){
            flightsArrFile.add(new HBoxCell(flight.getDestination(), flight.getDate(), flight.getNoSeats()+"", flight.getFlight_id()));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        flightsList.setItems(myObservableList);
    }

    protected int switchPane(String pane) {

        switch (pane) {
            case "reserved":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(true);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                newFlightPane.setVisible(false);
                return 0;

            case "flights":
                defaultPane.setVisible(false);
                flightsPane.setVisible(true);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                newFlightPane.setVisible(false);
                return 1;

            case "booked":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(true);
                ticketPane.setVisible(false);
                newFlightPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                return 2;

            case "adminFlights":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                newFlightPane.setVisible(false);
                adminFlightsPane.setVisible(true);
                return 3;

            case "default":
                defaultPane.setVisible(true);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                newFlightPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                return 4;

            case "details":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(true);
                sidePane.setVisible(false);
                newFlightPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                return 5;

            case "newFlight":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                sidePane.setVisible(false);
                newFlightPane.setVisible(true);
                adminFlightsPane.setVisible(false);
                return 5;

            case "None":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                reservedPane.setVisible(false);
                bookedPane.setVisible(false);
                ticketPane.setVisible(false);
                newFlightPane.setVisible(false);
                adminFlightsPane.setVisible(false);
                return 6;

            default:
                return -1;
        }
    }

    public void refreshFlightsList() throws IOException {
        flightsList.setItems(null);
        onFlightsBtnClicked();
    }
    public void refreshReservedList() throws IOException {
        reservedList.setItems(null);
        onReservedBtnClicked();
    }

    public void refreshAdminList() throws IOException {
        adminFlightsList.setItems(null);
        onAdminFlightsBtnClicked();
    }

    public class HBoxCell extends HBox {
        Label dest = new Label();
        Label date = new Label();
        Label seats = new Label();
        Label flightID = new Label();
        Label price = new Label();

        Label seatType = new Label();

        HBoxCell(String label){
            super();
            this.dest.setText(label);
            this.dest.setStyle("-fx-text-fill: #fd5b48");
            this.dest.setAlignment(Pos.CENTER);
            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(this.dest);
        }

        HBoxCell(String destination, String flightID) throws IOException {
            super();

            this.setSpacing(330);

            this.dest.setText(destination);
            this.flightID.setText(flightID);

            this.dest.setAlignment(Pos.CENTER);
            this.flightID.setAlignment(Pos.CENTER);

            this.dest.setMinWidth(100);

            this.dest.setMaxWidth(100);
            HBox.setHgrow(this.dest, Priority.ALWAYS);
            this.flightID.setMaxWidth(100);
            HBox.setHgrow(this.flightID, Priority.ALWAYS);

            this.dest.setFont(new Font(17));
            this.flightID.setFont(new Font(17));

            Flight f = new Flight(flightID);

            Button statusBtn;

            if(f.getStatus()){
                // flight is delayed
                statusBtn = new Button("On Time");
                statusBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
                statusBtn.setOnMouseEntered(e -> statusBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
                statusBtn.setOnMouseExited(e -> statusBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            }
            else{
                statusBtn = new Button("Delay");
                statusBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
                statusBtn.setOnMouseEntered(e -> statusBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
                statusBtn.setOnMouseExited(e -> statusBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            }

            statusBtn.setCursor(Cursor.HAND);
            statusBtn.setAlignment(Pos.CENTER);
            statusBtn.setOnAction(e -> {
                try {
                    Flight.updateDelayed(flightID, !f.getStatus());
                    refreshAdminList();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            this.getChildren().addAll(this.dest, this.flightID, statusBtn);
        }

        HBoxCell(String destination, String date, String noSeats, String flightID) {
            super();
            ComboBox<String> seatType = new ComboBox<>();

            // Add items to the ComboBox
            seatType.getItems().addAll(
                    "Economy",
                    "First",
                    "Business"
            );

//            seatType.getItems()

            // Set a default value
            seatType.setValue("Economy");

            seatType.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");


            this.setSpacing(90);

            this.dest.setText(destination);
            this.flightID.setText(flightID);
            this.date.setText(date);
            this.seats.setText(noSeats);

            this.dest.setAlignment(Pos.CENTER);
            this.flightID.setAlignment(Pos.CENTER);
            this.date.setAlignment(Pos.CENTER);
            this.seats.setAlignment(Pos.CENTER);

            this.dest.setMinWidth(100);

            this.dest.setMaxWidth(100);
            HBox.setHgrow(this.dest, Priority.ALWAYS);
            this.flightID.setMaxWidth(100);
            HBox.setHgrow(this.flightID, Priority.ALWAYS);
            this.date.setMaxWidth(100);
            HBox.setHgrow(this.date, Priority.ALWAYS);
            this.seats.setMaxWidth(100);
            HBox.setHgrow(this.seats, Priority.ALWAYS);
            seatType.setMaxWidth(120);
            HBox.setHgrow(this.seats, Priority.ALWAYS);

            this.dest.setFont(new Font(17));
            this.flightID.setFont(new Font(17));
            this.date.setFont(new Font(17));
            this.seats.setFont(new Font(17));
//            seatType.setStyle("-fx-font-size: 17;");

            Button reserveBtn = new Button("Reserve");

            if(Integer.parseInt(this.seats.getText()) <= 0){
                reserveBtn.setDisable(true);
            }

            reserveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
            reserveBtn.setOnMouseEntered(e -> reserveBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            reserveBtn.setOnMouseExited(e -> reserveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            reserveBtn.setCursor(Cursor.HAND);
            reserveBtn.setOnAction(e -> {
                try {
                    Flight f = new Flight(flightID);
                    f.reserveTicket(seatType.getValue() + " Class");

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // ADD A FUNCTION TO REFRESH THE LIST AND SHOW THE NEW DATA
                try {
                    refreshFlightsList();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                reserveBtn.setDisable(true);
            });

            this.getChildren().addAll(this.dest, this.flightID, this.date, this.seats, seatType, reserveBtn);
        }

        // Booked Flghts List constructor
        HBoxCell(String destination, String ticketID, String seatType) throws IOException {
            super();

            this.setSpacing(190);

            this.dest.setText(destination);
            Label ticketLabel = new Label(ticketID);
            this.seatType.setText(seatType);

            this.dest.setAlignment(Pos.CENTER);
            ticketLabel.setAlignment(Pos.CENTER);
            this.seatType.setAlignment(Pos.CENTER);

            this.dest.setFont(new Font(17));
            ticketLabel.setFont(new Font(17));
            this.seatType.setFont(new Font(17));

            this.dest.setMaxWidth(90);
            this.dest.setMinWidth(80);

            ticketLabel.setMaxWidth(100);
            ticketLabel.setMinWidth(80);
            this.seatType.setMaxWidth(150);
            this.seatType.setMinWidth(120);

            HBox.setHgrow(this.dest, Priority.ALWAYS);
            HBox.setHgrow(this.flightID, Priority.ALWAYS);
            HBox.setHgrow(this.seatType, Priority.ALWAYS);

            Button detailsBtn = new Button("Details");
            detailsBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");

            Flight f = new Flight(ticketID.split("_")[0]);

            detailsBtn.setOnAction(e -> {
                switchPane("details");

                ticket_destination.setText(f.getDestination());
                ticket_ticketID.setText(ticketID);
                ticket_flightDate.setText(f.getDate());
                ticket_reserver.setText(User.name);
                ticket_flightID.setText(f.getFlight_id());
                ticket_flightDeparture.setText(f.getDeparture_time());
                ticket_flightArrival.setText(f.getArrival_time());
                ticket_flightStatus.setText(f.getStatus() ? "DELAYED" : "ON TIME");
            });

            this.getChildren().addAll(this.dest, ticketLabel, this.seatType, detailsBtn);
        }

        // Reserved Flights List constructor
        HBoxCell(String destination,  String flightID, String seatType, int price, String ticketID) throws IOException {
            super();
            this.setSpacing(100);

            this.dest.setText(destination);
            this.flightID.setText(flightID);
            this.seatType.setText(seatType);
            this.price.setText(price+"");

            this.dest.setAlignment(Pos.CENTER);
            this.flightID.setAlignment(Pos.CENTER);
            this.seatType.setAlignment(Pos.CENTER);
            this.price.setAlignment(Pos.CENTER);

            this.dest.setMaxWidth(100);
            HBox.setHgrow(this.dest, Priority.ALWAYS);
            this.flightID.setMaxWidth(100);
            HBox.setHgrow(this.flightID, Priority.ALWAYS);
            this.seatType.setMaxWidth(130);
            HBox.setHgrow(this.seatType, Priority.ALWAYS);
            this.price.setMaxWidth(100);
            HBox.setHgrow(this.price, Priority.ALWAYS);

//            HBox.setMargin(this.price, new Insets(0, 0, 0, -100));
////            HBox.setMargin(this.date, new Insets(0, 100, 0, 0));
//            HBox.setHgrow(this.price, Priority.ALWAYS);

            this.dest.setFont(new Font(17));
            this.flightID.setFont(new Font(17));
            this.seatType.setFont(new Font(17));
            this.price.setFont(new Font(17));

            HBox buttons = new HBox();

            Button bookBtn = new Button("Book");
            bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
            bookBtn.setOnMouseEntered(e -> bookBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setOnMouseExited(e -> bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setCursor(Cursor.HAND);

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
            cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            cancelBtn.setCursor(Cursor.HAND);

            Flight f = new Flight(flightID);

            bookBtn.setOnAction(e -> {
                try {
                    f.bookTicket(ticketID);
                    refreshReservedList();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            cancelBtn.setOnAction(e -> {
                System.out.println("This button fired");
                try {
                    f.cancelTicket(ticketID);
                    refreshReservedList();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            buttons.getChildren().addAll(bookBtn, cancelBtn);
            buttons.setSpacing(3);

            this.getChildren().addAll(this.dest, this.flightID, this.seatType, this.price, buttons);
        }
    }

    public static int removeSeat(String destination) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/Flights.txt"));

        StringBuilder sb = new StringBuilder();
        String line;

        int remainingSeats = 0;

        while((line = file.readLine()) != null){
            String[] parts = line.split(":");
            if(Objects.equals(parts[0], destination)){
                line = parts[0] + ":" + parts[1] + ":" + (Integer.parseInt(parts[2]) - 1) + ":" + parts[3];
                remainingSeats = Integer.parseInt(parts[2]) - 1;
            }
            sb.append(line).append("\n");
        }
        file.close();

//        System.out.println(sb);

        PrintWriter writer = new PrintWriter(new FileWriter("src/main/java/com/example/testingmajortask/Database/Flights.txt"));
        writer.write(sb.toString());
        writer.close();
        return remainingSeats; // return the number of remaining seats
    }
}