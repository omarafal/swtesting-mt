module com.example.testingmajortask {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.jfoenix;

    opens com.example.testingmajortask to javafx.fxml;
    exports com.example.testingmajortask;
}