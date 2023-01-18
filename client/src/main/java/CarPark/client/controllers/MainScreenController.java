package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.events.NewSubscriberEvent;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


public class MainScreenController {

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
