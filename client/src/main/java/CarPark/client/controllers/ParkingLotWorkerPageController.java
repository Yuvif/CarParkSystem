package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Employee;
import CarPark.entities.ParkingLotWorker;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        Platform.runLater(()-> {
            try {
                SimpleChatClient.setRoot("ManagerParkingLotMap");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        ParkingLotWorker current_worker = (ParkingLotWorker) SimpleClient.getCurrent_user();
        userName.setText(current_worker.getFirstName());
        area.setText(current_worker.getParkinglot().getName());
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        LoginMessage msg = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT, SimpleClient.getCurrent_user().getId());
        SimpleClient.getClient().sendToServer(msg);
    }

    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException {

        switch (new_message.response_type) {
            case LOGOUT_SUCCEED:
                Platform.runLater(()-> {
                    try {
                        SimpleChatClient.setRoot("Login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

}
