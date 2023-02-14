package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.ParkingLotWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ParkingLotWorkerPageController {

    @FXML
    private Label area;

    @FXML
    private Button mapBtn;

    @FXML
    private Label userName;

    @FXML
    void showMap(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("ManagerParkingLotMap");
    }

    @FXML
    void initialize() throws IOException {
        ParkingLotWorker current_worker = (ParkingLotWorker) SimpleClient.getCurrent_user();
        userName.setText(current_worker.getFirstName());
        area.setText(current_worker.getParkinglot().getName());
    }

}
