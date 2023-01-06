/**
 * Sample Skeleton for 'CheckInGuest.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CheckedIn;
import il.cshaifasweng.OCSFMediatorExample.entities.ParkingSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
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

public class CheckInGuestController extends Controller{



    @FXML // fx:id="checkInB"
    private Button checkInB; // Value injected by FXMLLoader

    @FXML // fx:id="hourTf"
    private TextField hourTf; // Value injected by FXMLLoader

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

    private List<Parkinglot> parkinglots = new LinkedList<Parkinglot>();


    @FXML
    void initialize() {
        getParkingLots();
    }

    Parkinglot getSelectedParkingLot() {
        Parkinglot pickedParkingLot = new Parkinglot();
        for (Parkinglot s : parkinglots)
            if (s.getId().equals(plPick.getValue()))
                pickedParkingLot = s;
        return pickedParkingLot;
    }

    // Setting the Parkinglots from database into the choosebox
    private void getParkingLots() {
        this.parkinglots = SimpleChatClient.client.getParkinglots();
        plPick.getItems().add("Set ParkingLot");
        plPick.setValue("Set ParkingLot");
        for (Parkinglot s : parkinglots)
            plPick.getItems().add(s.getId());
    }

    // Getting the first available open ParkingSlot ;
    private ParkingSlot getParkingSlot()
    {
        List<ParkingSlot> parkingSlots = getSelectedParkingLot().getParkingSlots();;
        for(ParkingSlot slot : parkingSlots)
        {
            if(slot.getSpotStatus()== ParkingSlot.Status.EMPTY)
            {
               return slot;
            }
        }
        return null;
    }
    @FXML
    void checkIn(ActionEvent event) {
        if (checkEmpty()) {
            List<Object> msg = new LinkedList<>();
            msg.add("#CHECKINGIN");
            ParkingSlot slot = getParkingSlot();
            slot.setSpotStatus(ParkingSlot.Status.USED);
            CheckedIn checkedIn = new CheckedIn( new Date(), Integer.parseInt(idTf.getText()), Integer.parseInt(car_nTf.getText()), mailTf.getText(), new Date(),slot);
            msg.add(checkedIn);
            msg.add(slot);


            try {
                SimpleChatClient.client.sendToServer(msg);
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
    private boolean checkEmpty() {
        if (idTf.getText().isEmpty() || car_nTf.getText().isEmpty() || mailTf.getText().isEmpty() || hourTf.getText().isEmpty()) {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        if(plPick.getValue().equals("Set ParkingLot")){
            sendAlert("Please select a parking lot", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        if (!(idTf.getText().matches("[0-9]*")))
        {
            sendAlert("ID can only contain numbers", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        if (!(car_nTf.getText().matches("[0-9]*")))
        {
            sendAlert("Car number can only contain numbers", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    private void screenChangeMain() throws IOException {
        SimpleChatClient.setRoot("MainScreenTest");
    }
}
