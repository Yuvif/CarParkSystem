package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class EmployeePageController {
    @FXML
    private Label welcomeUser;

    @FXML
    void initialize() throws IOException {
        welcomeUser.setText("Welcome " + SimpleChatClient.user.getFirstName());
    }

    @Subscribe
    void initEmployee(){
    }

}
