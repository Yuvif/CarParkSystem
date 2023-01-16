package CarPark.client.controllers;

import CarPark.client.SimpleClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class EmployeePageController{
    @FXML
    private Label welcomeUser;

    @FXML
    void initialize() throws IOException {
        welcomeUser.setText("Welcome " + SimpleClient.getCurrent_user().getFirstName());
    }

}
