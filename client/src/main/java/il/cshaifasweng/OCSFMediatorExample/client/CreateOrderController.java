package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static il.cshaifasweng.OCSFMediatorExample.client.Controller.sendAlert;

public class CreateOrderController {

    @FXML
    private MenuButton arrivalHour;

    @FXML
    private MenuButton arrivalMin;

    @FXML
    private DatePicker arrivalDate;

    @FXML
    private TextField carIdTextBox;

    @FXML
    private TextField emailAddress;

    @FXML
    private DatePicker estLeavingDate;

    @FXML
    private MenuButton estLeavingHour;

    @FXML
    private MenuButton estLeavingMin;

    @FXML
    private TextField idTextBox;

    @FXML
    private MenuButton parkingLotsOpt;

    @FXML
    private Button submitBtn;

    @FXML
    void chooseArrivalDate(ActionEvent event) {

    }

    @FXML
    void chooseArrivalHour(ActionEvent event) {

        MenuItem[] hours = new MenuItem[24];
        for(int i = 0; i < 24; i++)
        {
            hours[i] = new MenuItem(Integer.toString(i));
            arrivalHour.getItems().add(hours[i]);
        }
    }

    @FXML
    void chooseArrivalMin(ActionEvent event)
    {
        MenuItem[] menuItems = new MenuItem[60];
        for(int i = 0; i < 60; i++)
        {
            menuItems[i] = new MenuItem(Integer.toString(i));
            arrivalMin.getItems().add(menuItems[i]);
        }
    }

    @FXML
    void chooseEstLeavingHour(ActionEvent event)
    {
        MenuItem[] hours = new MenuItem[24];
        for(int i = 0; i < 24; i++)
        {
            hours[i] = new MenuItem(Integer.toString(i));
            arrivalHour.getItems().add(hours[i]);
        }
    }

    @FXML
    void chooseEstLeavingMin(ActionEvent event)
    {
        MenuItem[] menuItems = new MenuItem[60];
        for(int i = 0; i < 60; i++)
        {
            menuItems[i] = new MenuItem(Integer.toString(i));
            arrivalMin.getItems().add(menuItems[i]);
        }
    }

    @FXML
    void chooseEstimatedLeavingDate(ActionEvent event) {

    }

    @FXML
    void chooseParkingLot(ActionEvent event) {

    }

    @FXML
    void enterCarId(ActionEvent event) {

    }

    @FXML
    void enterCustomerId(ActionEvent event) {

    }

    @FXML
    void enterEmail(ActionEvent event) {

    }

    @FXML
    void submitDetails(ActionEvent event) {

    }


    private boolean checkEmpty()
    {
        if (carIdTextBox.getText().isEmpty() || emailAddress.getText().isEmpty() || idTextBox.getText().isEmpty()) {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

}
