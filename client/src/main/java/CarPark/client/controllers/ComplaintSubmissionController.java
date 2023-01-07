/**
 * Sample Skeleton for 'ComplaintSubmission.fxml' Controller Class
 */

package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.entities.Complaint;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Duration;

public class ComplaintSubmissionController extends Controller {

    @FXML // fx:id="complaintDesc"
    private TextField complaintDesc; // Value injected by FXMLLoader

    @FXML // fx:id="sendComplaintBtn"
    private Button sendComplaintBtn; // Value injected by FXMLLoader


//    @FXML
//    void sendComplaint(ActionEvent event) {
//        if (checkEmpty()) {
//            List<Object> msg = new LinkedList<>();
//            msg.add("#COMPLAINT");
//            Complaint complaint = new Complaint(new Date(), complaintDesc.getText());
//            msg.add(complaint);
//            try {
//                SimpleChatClient.client.sendToServer(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Platform.runLater(() -> {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setHeaderText("Complaint submitted");
//                alert.show();
//                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
//                pause.setOnFinished((e -> {
//                    alert.close();
//                }));
//                pause.play();
//            });
//        }
//
//    }


//    private boolean checkEmpty() {
//        if (complaintDesc.getText().isEmpty()) {
//            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void handleMessage() {
//
//    }
}
