package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class CustomerPageController{
    @FXML
    private Label welcomeUser;
    @FXML
    private Button newMember;

    @FXML
    public void registerAsMember(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("RegisterAsMember");
    }

    @FXML
    public void createOrder(){}
    @FXML
    public void addComplaint(){}
    @FXML
    public void myMemberships(){}
    @FXML
    public void myOrders(){}

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        welcomeUser.setText("Welcome " + SimpleClient.getCurrent_user().getFirstName());
    }

    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
    }



}
