package com.example.testingmajortask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label totalFareLabel;
    @FXML
    public Label username;

    @FXML
    TextField searchBar;

    @FXML
    ListView<HBoxCell> flightsList;

    @FXML
    ListView<HBoxCell> reservedList;

    // panes
    @FXML
    VBox defaultPane;
    @FXML
    VBox flightsPane;
    @FXML
    VBox profilePane;
    @FXML
    VBox sidePane;
    @FXML
    VBox printPane;
    @FXML
    AnchorPane ticketPane;

    public void initialize() {
        username.setText(User.name);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

//    @FXML
//    protected void onPrintButtonClick() {
//        WritableImage snapshot = printPane.snapshot(null, null);
//        ImageView imageView = new ImageView(snapshot);
//
//        Label jobStatus = new Label();
//        jobStatus.textProperty().unbind();
//        jobStatus.setText("Creating a printer job...");
//
//        PrinterJob job = PrinterJob.createPrinterJob();
//        if (job != null) {
//            // Center the node within the printable area
////            double printableWidth = job.getJobSettings().getPageLayout().getPrintableWidth();
////            double printableHeight = job.getJobSettings().getPageLayout().getPrintableHeight();
////            double nodeWidth = nodeToPrint.getBoundsInParent().getWidth();
////            double nodeHeight = nodeToPrint.getBoundsInParent().getHeight();
////            nodeToPrint.setLayoutX((printableWidth - nodeWidth) / 2);
////            nodeToPrint.setLayoutY((printableHeight - nodeHeight) / 2);
//
//            jobStatus.textProperty().bind(job.jobStatusProperty().asString());
//            boolean printed = job.printPage(imageView);
////            printPane.setVisible(false);
//
//            if (printed) {
//                job.endJob();
//            } else {
//                jobStatus.textProperty().unbind();
//                jobStatus.setText("Printing failed.");
//            }
//        } else {
//            jobStatus.setText("Could not create a printer job.");
//        }
//    }

    @FXML
    protected void onSearchBtnClicked() {
        String text = searchBar.getText(); // text in search bar field
//        if(text.isEmpty()){
//            System.out.println("This is empty!");
//        }

    }

    // functions that switch main menu panes
    @FXML
    protected void onProfileBtnClicked() throws IOException {
        int total_fare = 0;

        switchPane("profile");
        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>();
        String line;
        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/BookedFlights.txt"));
        while ((line = file.readLine()) != null) {

            // skip first line
            if (Objects.equals(line.split(":")[0], "Name") || !Objects.equals(line.split(":")[0], User.name)) {
                continue;
            }

            String[] parts = line.split(":");

            total_fare += Integer.parseInt(parts[4]);

            flightsArrFile.add(new HBoxCell(parts[1], parts[2], parts[4]));
        }
        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        reservedList.setItems(myObservableList);
        totalFareLabel.setText("$" + total_fare);
    }

    @FXML
    protected void onReturnBtnClicked(){
        sidePane.setVisible(true);
        switchPane("flights");
        ticketPane.setVisible(false);
    }

    @FXML
    protected void onFlightsBtnClicked() throws IOException {

        switchPane("flights");

        ArrayList<HBoxCell> flightsArrFile = new ArrayList<>(); // list.add(new HBoxCell())
        String line;
        BufferedReader file = new BufferedReader(new FileReader("src/main/java/com/example/testingmajortask/Database/Flights.txt"));

        while ((line = file.readLine()) != null) {

            // skip first line
            if (Objects.equals(line.split(":")[0], "Dest")) {
                continue;
            }

            String[] parts = line.split(":");


            flightsArrFile.add(new HBoxCell(parts[0], parts[1], parts[2], parts[3]));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(flightsArrFile);
        flightsList.setItems(myObservableList);

    }

    protected int switchPane(String pane) {

        switch (pane) {
            case "profile":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                profilePane.setVisible(true);
                return 0;

            case "flights":
                defaultPane.setVisible(false);
                flightsPane.setVisible(true);
                profilePane.setVisible(false);
                return 1;

            case "default":
                defaultPane.setVisible(true);
                flightsPane.setVisible(false);
                profilePane.setVisible(false);
                return 2;

            case "None":
                defaultPane.setVisible(false);
                flightsPane.setVisible(false);
                profilePane.setVisible(false);
                return 3;

            default:
                return -1;
        }
    }

    public void refreshFlightsList() throws IOException {
        flightsList.setItems(null);
        onFlightsBtnClicked();
    }

    public class HBoxCell extends HBox {
        Label dest = new Label();
        Label date = new Label();
        Label seats = new Label();
        Label price = new Label();

        HBoxCell(String destination, String date, String seats, String price) {
            super();
            this.setSpacing(100);

            this.dest.setText(destination);
            this.date.setText(date);
            this.seats.setText(seats);
            this.price.setText(price);

            this.dest.setAlignment(Pos.CENTER);
            this.date.setAlignment(Pos.CENTER);
            this.seats.setAlignment(Pos.CENTER);
            this.price.setAlignment(Pos.CENTER);

            this.dest.setMaxWidth(100);
            HBox.setHgrow(this.dest, Priority.ALWAYS);
            this.date.setMaxWidth(100);
            HBox.setHgrow(this.date, Priority.ALWAYS);
            this.seats.setMaxWidth(100);
            HBox.setHgrow(this.seats, Priority.ALWAYS);
            this.price.setMaxWidth(100);
            HBox.setHgrow(this.price, Priority.ALWAYS);

            this.dest.setFont(new Font(17));
            this.date.setFont(new Font(17));
            this.seats.setFont(new Font(17));
            this.price.setFont(new Font(17));

            Button bookBtn = new Button("Book");


            if(Integer.parseInt(this.seats.getText()) <= 0){
                bookBtn.setDisable(true);
            }


            bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
            bookBtn.setOnMouseEntered(e -> bookBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setOnMouseExited(e -> bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setCursor(Cursor.HAND);
            bookBtn.setOnAction(e -> {
                try {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = currentDate.format(formatter);

                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/java/com/example/testingmajortask/Database/BookedFlights.txt", true));

                    bufferedWriter.write(User.name + ":" + this.dest.getText() + ":" + this.date.getText() + ":" + formattedDate + ":" + this.price.getText());

                    // ADD LOGIC TO REDUCE NUMBER OF SEATS OF THIS FLIGHT
                    removeSeat(this.dest.getText());

                    bufferedWriter.newLine();
                    bufferedWriter.close();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // ADD A FUNCTION TO REFRESH THE LIST AND SHOW THE NEW DATA
                try {
                    refreshFlightsList();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                bookBtn.setDisable(true);
            });

            this.getChildren().addAll(this.dest, this.date, this.seats, this.price, bookBtn);
        }
        HBoxCell(String destination, String date, String price) {
            super();
            this.setSpacing(150);

            this.dest.setText(destination);
            this.date.setText(date);
            this.price.setText(price);

            this.dest.setAlignment(Pos.CENTER);
            this.date.setAlignment(Pos.CENTER);
            this.price.setAlignment(Pos.CENTER);

            this.dest.setMaxWidth(100);
            HBox.setHgrow(this.dest, Priority.ALWAYS);
            this.date.setMaxWidth(100);
            HBox.setHgrow(this.date, Priority.ALWAYS);
            this.price.setMaxWidth(100);
//
            HBox.setMargin(this.price, new Insets(0, 0, 0, -100));
//            HBox.setMargin(this.date, new Insets(0, 100, 0, 0));
            HBox.setHgrow(this.price, Priority.ALWAYS);

            this.dest.setFont(new Font(17));
            this.date.setFont(new Font(17));
            this.price.setFont(new Font(17));

            Button bookBtn = new Button("Details");

            bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;");
            bookBtn.setOnMouseEntered(e -> bookBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setOnMouseExited(e -> bookBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 9pt; -fx-padding: 8 17;"));
            bookBtn.setCursor(Cursor.HAND);
            bookBtn.setOnAction(e -> {
                sidePane.setVisible(false);
                switchPane("None");
                ticketPane.setVisible(true);
            });

            this.getChildren().addAll(this.dest, this.date, this.seats, this.price, bookBtn);
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