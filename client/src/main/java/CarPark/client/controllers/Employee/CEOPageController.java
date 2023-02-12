package CarPark.client.controllers.Employee;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.CEO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class CEOPageController {
    @FXML
    private Label userName;

    @FXML
    void initialize() throws IOException {
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

}
