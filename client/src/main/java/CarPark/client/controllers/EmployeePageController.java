package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class EmployeePageController{
    @FXML
    private Label welcomeUser;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        welcomeUser.setText("Welcome " + SimpleClient.getCurrent_user().getFirstName());
    }

    @FXML
    private void createReports(javafx.event.ActionEvent event) throws IOException {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("CreateReports");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
    }

    @FXML
    private void complaintsInspection(javafx.event.ActionEvent event) throws IOException {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("ComplaintInspectionTable");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}