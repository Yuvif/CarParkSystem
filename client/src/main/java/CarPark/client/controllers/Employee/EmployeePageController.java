package CarPark.client.controllers.Employee;

import CarPark.client.SimpleChatClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class EmployeePageController {

    @FXML
    void prices(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("Login");
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
                SimpleChatClient.setRoot("Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
