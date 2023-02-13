package CarPark.client.controllers.Employee.Manager;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Manager;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class ParkingLotManagerController {
    @FXML
    private Label userName;
    @FXML
    private Label area;


    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
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
    void logout(ActionEvent event) throws IOException {
        LoginMessage msg = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,SimpleClient.getCurrent_user().getId());
        SimpleClient.getClient().sendToServer(msg);
    }

    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case LOGOUT_SUCCEED:
                Platform.runLater(() ->
                {
                    try {
                        SimpleChatClient.setRoot("Login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    public void showParkingLotMap(ActionEvent event) throws IOException {

        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("ParkingLotMap");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
