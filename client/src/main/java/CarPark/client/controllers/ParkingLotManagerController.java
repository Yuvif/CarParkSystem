package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Manager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class ParkingLotManagerController {
    @FXML
    private Label userName;
    @FXML
    private Label area;


    @FXML
    void initialize() throws IOException {
        Manager current_manager = (Manager) SimpleClient.getCurrent_user();
        userName.setText(current_manager.getFirstName());
        area.setText(current_manager.getParkinglot().getName());
    }

    @FXML
    void prices(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("PricesManager");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void makeReport(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("CreateReports");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void showParkingLotMap(ActionEvent actionEvent) throws IOException {
        SimpleChatClient.setRoot("ParkingLotMap");
    }

    @Subscribe
    public void nada(){}


}