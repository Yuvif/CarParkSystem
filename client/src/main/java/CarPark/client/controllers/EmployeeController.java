package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.entities.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class EmployeeController {
    @FXML
    private Label welcomeUser;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        welcomeUser.setText(SimpleChatClient.employee.getFirstName());
    }

    @Subscribe
    void nada(){

    }

}
