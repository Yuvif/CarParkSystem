package CarPark.client.controllers.Employee;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
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


    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
    }

}