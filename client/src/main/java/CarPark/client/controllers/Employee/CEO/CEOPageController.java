package CarPark.client.controllers.Employee.CEO;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.CEO;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class CEOPageController {
    @FXML
    private Label userName;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        CEO ceo = (CEO) SimpleClient.getCurrent_user();
        userName.setText(ceo.getFirstName());
    }

    @FXML
    void prices(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("PricesCEO");
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

    @FXML
    void makeReports(ActionEvent event) throws IOException{
        SimpleChatClient.setRoot("CEOReports");
    }

    @FXML
    void parkingLotMap(ActionEvent event) throws IOException{
        SimpleChatClient.setRoot("CEOParkingLotMap");
    }

    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case LOGOUT_SUCCEED:
                Platform.runLater(()->
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
