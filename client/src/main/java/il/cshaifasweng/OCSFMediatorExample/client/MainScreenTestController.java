/**
 * Sample Skeleton for 'MainScreenTest.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainScreenTestController extends Controller {

    @FXML // fx:id="checkInBtn"
    private Button checkInBtn; // Value injected by FXMLLoader

    @FXML
    void initialize() {

        SimpleChatClient.client.setController(this);
        List<Object> msg = new LinkedList<>();
        msg.add("#PULLPARKINGLOTS");

        try {
            SimpleChatClient.client.sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void screenChangeCheckIn() throws IOException {
        SimpleChatClient.setRoot("CheckInGuest");
    }

}
