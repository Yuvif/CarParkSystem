package CarPark.client.controllers;

import java.io.IOException;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainScreenController {
    private int msgId;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
    }

   @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
    }

    @FXML
    private void Parking() throws IOException {
        SimpleChatClient.setRoot("ParkingList");
    }

    @FXML
    private void PricesTable() throws IOException {
        SimpleChatClient.setRoot("PricesTable");
    }


}
