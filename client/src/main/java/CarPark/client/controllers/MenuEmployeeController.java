package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import CarPark.entities.messages.ParkingSlotsMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javafx.event.ActionEvent;

public class MenuEmployeeController extends Controller {

    @FXML // fx:id="editParkingSlots"
    private Button editParkingSlots; // Value injected by FXMLLoader

    @FXML
    void initialize() throws IOException {
        assert editParkingSlots != null : "fx:id=\"plPick\" was not injected: check your FXML file 'Login.fxml'.";

        //SimpleChatClient.client.setController(this);
//        ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
//        SimpleClient.getClient().sendToServer(msg);
    }
    @FXML
    void screenChangeChoosePL(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("EditParkingSlots");
    }

}