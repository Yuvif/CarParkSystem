/**
 * Sample Skeleton for 'CheckInGuest.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CheckInGuestController extends Controller{

    @FXML // fx:id="car_nTf"
    private TextField car_nTf; // Value injected by FXMLLoader

    @FXML // fx:id="checkInB"
    private Button checkInB; // Value injected by FXMLLoader

    @FXML // fx:id="hourTf"
    private TextField hourTf; // Value injected by FXMLLoader

    @FXML // fx:id="idTf"
    private TextField idTf; // Value injected by FXMLLoader

    @FXML // fx:id="mailTf"
    private TextField mailTf; // Value injected by FXMLLoader

    @FXML // fx:id="plPick"
    private ComboBox<String> plPick; // Value injected by FXMLLoader

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
    private void getParkingLots() {
        this.parkinglots = SimpleChatClient.client.getParkinglots();
        plPick.getItems().add("Set ParkingLot");
        plPick.setValue("Set ParkingLot");
        for (Parkinglot s : parkinglots)
            plPick.getItems().add(s.getId());
    }
    @FXML
    void checkIn(ActionEvent event) {

    }


}
