/**
 * Sample Skeleton for 'CheckInGuest.fxml' Controller Class
 */

package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.CheckInGuestMessage;
import CarPark.entities.CheckedIn;
import CarPark.entities.messages.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static CarPark.client.controllers.Controller.sendAlert;

public class CheckInGuestController extends Controller{



    @FXML // fx:id="checkInB"
    private Button checkInB; // Value injected by FXMLLoader

    @FXML // fx:id="idTf"
    private TextField idTf; // Value injected by FXMLLoader

    @FXML // fx:id="car_nTf"
    private TextField car_nTf; // Value injected by FXMLLoader

    @FXML // fx:id="mailTf"
    private TextField mailTf; // Value injected by FXMLLoader

    @FXML // fx:id="plPick"
    private ComboBox<String> plPick; // Value injected by FXMLLoader

    @FXML // fx:id="leavingTimeTF"
    private DatePicker leavingTimeTF; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        String[] plNames = new String[]{"CPS Haifa", "CPS Tel-Aviv",
                "CPS Be'er Sheva", "CPS Rehovot", "CPS Jerusalem", "CPS Eilat"};
        plPick.getItems().add("Set ParkingLot");
        plPick.setValue("Set ParkingLot");
        for (String n : plNames)
            plPick.getItems().add(n);
    }

    @FXML
    void checkIn(ActionEvent event) throws IOException {
        if (checkEmpty()) {
            CheckedIn checkedIn = createCheckedIn();
            CheckInGuestMessage message = new CheckInGuestMessage(Message.MessageType.REQUEST, CheckInGuestMessage.RequestType.CHECK_ME_IN, checkedIn);
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Welcome");
                alert.show();
                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                pause.setOnFinished((e -> {
                    alert.close();
                    try {
                        screenChangeMain();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));
                pause.play();
            });
        }
    }

    private CheckedIn createCheckedIn() {
        CheckedIn checkedIn = new CheckedIn( new Date(), Integer.parseInt(idTf.getText()), Integer.parseInt(car_nTf.getText()), mailTf.getText(), new Date(), new ParkingSlot());
        checkedIn.setParkinglot_name(plPick.getValue());
        return checkedIn;
    }
    private boolean checkEmpty() {
        if (idTf.getText().isEmpty() || car_nTf.getText().isEmpty() || mailTf.getText().isEmpty() || plPick.getValue() == null) {
            sendAlert("Please fill all the fields", "warning", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }
    @FXML
    private void screenChangeMain() throws IOException {
        SimpleChatClient.setRoot("MainScreenTest");
    }
}