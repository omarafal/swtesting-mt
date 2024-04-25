package com.example.testingmajortask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    JFXTextField usernameField;
    @FXML
    JFXPasswordField passwordField;
    @FXML
    JFXTextField passwordShown;
    @FXML
    Button showPassBtn;
    @FXML
    Label errmsgLable;
    @FXML
    JFXButton loginBtn;
    @FXML
    ImageView hideIco;
    @FXML
    ImageView showIco;
    boolean visible = false;
    @FXML
    AnchorPane anchorPane;


    @FXML
    public void enter1(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER))
            passwordField.requestFocus();
    }
    @FXML
    public void enter2(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)) {
            loginBtn.fire();
            loginBtn.requestFocus();
        }
    }

    @FXML
    protected void logIn() throws IOException {
        String password = passwordShown.getText().trim().isEmpty() ? passwordField.getText() : passwordShown.getText();

        if(User.loginUser(usernameField.getText(), password) != -1){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
            Scene mainScene = new Scene(root, 1131, 610);
            Stage window = (Stage)(loginBtn.getScene().getWindow());
            window.setTitle("AirReserve");
            window.setScene(mainScene);
            window.show();
        }
        else{
            errmsgLable.setText("You have entered wrong/non-existing credentials.");
            errmsgLable.setStyle("-fx-text-fill:red");
        }
    }

    public void showPass(){
        if(!visible){
            hideIco.setVisible(false);
            showIco.setVisible(true);
            passwordShown.setText(passwordField.getText());
            passwordShown.setVisible(true);
            passwordField.setVisible(false);
            visible=true;
        }else {
            hideIco.setVisible(true);
            showIco.setVisible(false);
            passwordField.setText(passwordShown.getText());
            passwordField.setVisible(true);
            passwordShown.setVisible(false);
            visible=false;
        }
    }
}