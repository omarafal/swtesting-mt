package com.example.testingmajortask;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class PrintTicket extends Thread {

    Node nodeToPrint;

    PrintTicket(Node node){
        nodeToPrint = node;
    }

    @Override
    public void run() {
        WritableImage snapshot = nodeToPrint.snapshot(null, null);
        ImageView imageView = new ImageView(snapshot);

        Label jobStatus = new Label();
        jobStatus.textProperty().unbind();
        jobStatus.setText("Creating a printer job...");

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            // Center the node within the printable area
//            double printableWidth = job.getJobSettings().getPageLayout().getPrintableWidth();
//            double printableHeight = job.getJobSettings().getPageLayout().getPrintableHeight();
//            double nodeWidth = nodeToPrint.getBoundsInParent().getWidth();
//            double nodeHeight = nodeToPrint.getBoundsInParent().getHeight();
//            nodeToPrint.setLayoutX((printableWidth - nodeWidth) / 2);
//            nodeToPrint.setLayoutY((printableHeight - nodeHeight) / 2);

            jobStatus.textProperty().bind(job.jobStatusProperty().asString());
            boolean printed = job.printPage(imageView);
            nodeToPrint.setVisible(false);

            if (printed) {
                job.endJob();
            } else {
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing failed.");
            }
        } else {
            jobStatus.setText("Could not create a printer job.");
        }
    }

//    public static void main(String[] args) {
//        MyThread thread = new MyThread();
//        thread.start(); // This starts the thread execution
//
//        System.out.println("This is running in the main thread!");
//    }
}