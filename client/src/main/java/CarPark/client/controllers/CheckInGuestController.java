package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.CheckedIn;
import CarPark.entities.messages.CheckInMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CheckInGuestController{



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
    private DatePicker estLeavingDate; // Value injected by FXMLLoader

    @FXML
    private ComboBox<java.io.Serializable> estLeavingHour;

    @FXML
    private ComboBox<java.io.Serializable> estLeavingMin;


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        String[] plNames = new String[]{"Haifa", "Tel Aviv","Jerusalem",
                "Be'er Sheva", "Eilat"};
        for (String n : plNames)
            plPick.getItems().add(n);
        // Initialize the ComboBox with the hours
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                estLeavingHour.getItems().add("0" + i);
            } else {
                estLeavingHour.getItems().add(i);
            }
        }

        // Initialize the ComboBox with the minutes
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                estLeavingMin.getItems().add("0" + i);
            } else {
                estLeavingMin.getItems().add(i);
            }
        }
        estLeavingDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                // Disable all past dates
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    @FXML
    void checkIn(ActionEvent event) throws IOException {
        if (checkEmpty()) {
            CheckedIn checkedIn = createCheckedIn();
            CheckInMessage message = new CheckInMessage(Message.MessageType.REQUEST, CheckInMessage.RequestType.CHECK_ME_IN_GUEST, checkedIn);
            message.selectedParkingLot = plPick.getValue();
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CheckedIn createCheckedIn() {
        LocalDate leaving = estLeavingDate.getValue();
        LocalTime leavingTime = LocalTime.of(Integer.parseInt(estLeavingHour.getValue().toString()), Integer.parseInt(estLeavingMin.getValue().toString()));
        LocalDateTime leavingDateTime = LocalDateTime.of(leaving, leavingTime);
        CheckedIn checkedIn = new CheckedIn(LocalDateTime.now(), Long.parseLong(idTf.getText()), Integer.parseInt(car_nTf.getText()), mailTf.getText(), leavingDateTime,"GUEST");
        return checkedIn;
    }
    private boolean checkEmpty() {
        if (idTf.getText().isEmpty() || car_nTf.getText().isEmpty() || mailTf.getText().isEmpty() || plPick.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please fill all the fields!");
            alert.show();
            return false;
        }

        return true;
    }

    @Subscribe
    public void newResponse(CheckInMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case CHECKED_IN_GUEST -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Welcome \n " +
                            "to CPS " + new_message.selectedParkingLot +"\n Your Parking Slot is: " + new_message.checkedIn.getParkingSlot().getId());
                    alert.show();
                });
            }
            case PARKING_LOT_IS_FULL -> Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("This parking lot is full at the moment \n " +
                        "You can go to CPS " + new_message.alternativeParkingLot + " instead");
                alert.show();
            });
        }
    }


    @FXML
    private void loginPage(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("Login");
    }
}