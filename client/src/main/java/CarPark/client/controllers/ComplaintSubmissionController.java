package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.entities.Complaint;
import CarPark.entities.messages.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import CarPark.entities.messages.ComplaintMessage;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;

public class ComplaintSubmissionController extends Controller {

    @FXML // fx:id="complaintDesc"
    private TextField complaintDesc; // Value injected by FXMLLoader

    @FXML // fx:id="sendComplaintBtn"
    private Button sendComplaintBtn; // Value injected by FXMLLoader

    @FXML
    private TextField customerIdT;

    @FXML
    private ComboBox<?> plPick;


    @FXML
    void sendComplaint(ActionEvent event) {
        EventBus.getDefault().register(this);
        if (checkEmpty()) {
//            create a complaint with the description
            Complaint complaint = new Complaint(new Date(), complaintDesc.getText(), Long.parseLong(customerIdT.getText()));
//            create a list with the complaint
//            send the complaint to the server
            ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.CREATE_NEW_COMPLAINT, complaint);
            try {
                SimpleChatClient.client.sendToServer(complaintMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Complaint submitted");
                alert.show();
                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                pause.setOnFinished((e -> {
                    alert.close();
                }));
                pause.play();
            });
        }
    }

    private boolean checkEmpty() {
        if (complaintDesc.getText().isEmpty()) {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}