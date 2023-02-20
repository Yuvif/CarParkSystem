package CarPark.client.controllers.Employee;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class CustomerServiceController {
    @FXML
    private Label userName;


    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
    }


    @FXML
    public void inspectComplaints(javafx.event.ActionEvent actionEvent) {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("ComplaintInspectionTable");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    public void makeReport(ActionEvent actionEvent) {
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
    void showParkingLotMap(ActionEvent event) {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("CEOParkingLotMap");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
}