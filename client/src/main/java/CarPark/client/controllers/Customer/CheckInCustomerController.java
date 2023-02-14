package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.CheckedIn;
import CarPark.entities.messages.CheckInMessage;
import CarPark.entities.messages.LoginMessage;
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

public class CheckInCustomerController {

    @FXML
    private TextField car_nTf;

    @FXML
    private Button checkInB;

    @FXML
    private DatePicker estLeavingDate;

    @FXML
    private ComboBox<java.io.Serializable> estLeavingHour;

    @FXML
    private ComboBox<java.io.Serializable> estLeavingMin;

    @FXML
    private TextField idTf;

    @FXML
    private ComboBox<String> plPick;

    @FXML
    private Label userName;

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        idTf.setText(SimpleClient.getCurrent_user().getId());
        idTf.setDisable(true);
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

    private CheckedIn createCheckedIn() {
        LocalDate leaving = estLeavingDate.getValue();
        LocalTime leavingTime = LocalTime.of(Integer.parseInt(estLeavingHour.getValue().toString()), Integer.parseInt(estLeavingMin.getValue().toString()));
        LocalDateTime leavingDateTime = LocalDateTime.of(leaving, leavingTime);
        CheckedIn checkedIn = new CheckedIn(LocalDateTime.now(), idTf.getText(), Integer.parseInt(car_nTf.getText()),"EMAIL@EMAIL.COM", leavingDateTime,"CUSTOMER");
        return checkedIn;
    }
    private boolean checkEmpty() {
        if (idTf.getText().isEmpty() || car_nTf.getText().isEmpty() || plPick.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please fill all the fields!");
            alert.show();
            return false;
        }

        return true;
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


    @FXML
    void logout(ActionEvent event) throws IOException {
        LoginMessage msg = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT, SimpleClient.getCurrent_user().getId());
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void myOrders(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("MyOrders");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    void createNewOrder(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("CreateOrder");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void newComplaint(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("ComplaintSubmission");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void myComplaints(ActionEvent event) throws IOException{
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("MyComplaints");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void myMemberships(ActionEvent event) throws IOException {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("MembershipsView");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void newMembership(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("RegisterAsMember");
                EventBus.getDefault().unregister(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case LOGOUT_SUCCEED:
                Platform.runLater(()->
                {
                    try {
                        SimpleChatClient.setRoot("Login");
                        EventBus.getDefault().unregister(this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }

    }
}
