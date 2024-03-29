package CarPark.client.controllers.Customer;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.Membership;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.MembershipMessage;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static CarPark.client.controllers.Controller.sendAlert;

public class RegisterAsMemberController {//Daniel need to change name to new member reg, also in server...

    @FXML
    private Label userName;
    @FXML
    private ComboBox<java.io.Serializable> arrivalHour;
    @FXML
    private ComboBox<java.io.Serializable> arrivalMin;
    @FXML
    private TextField customerIdText;
    @FXML
    private ComboBox<String> parkingLots;
    @FXML
    private Button registerBtn;
    @FXML
    private DatePicker startDateOfMembership;
    @FXML
    private ComboBox<String> membershipOpt;
    @FXML
    private TextField carIdNumber;

    @FXML
    void goBack(ActionEvent event) throws IOException
    {
        SimpleChatClient.setRoot("CustomerPage");
    }

    @FXML
    void registerToSystem(ActionEvent event) throws IOException
    {
        if (checkValidity()) // create membership entity and send it to the server
        {
            Membership membership = createMembership();
            MembershipMessage msg = new MembershipMessage(Message.MessageType.REQUEST, MembershipMessage.RequestType.REGISTER, membership,
                    (Customer)SimpleClient.getCurrent_user());
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void chooseMembership(ActionEvent event)
    {
        if(Objects.equals(membershipOpt.getValue(), "Full Membership"))
        {
            parkingLots.setDisable(true);
            arrivalHour.setDisable(true);
            arrivalMin.setDisable(true);
        }
        else
        {
            parkingLots.setDisable(false);
            arrivalHour.setDisable(false);
            arrivalMin.setDisable(false);
        }
    }


    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        parkingLots.getItems().add("Haifa");
        parkingLots.getItems().add("Tel Aviv");
        parkingLots.getItems().add("Jerusalem");
        parkingLots.getItems().add("Be'er Sheva");
        parkingLots.getItems().add("Eilat");
        customerIdText.setText(SimpleClient.getCurrent_user().getId().toString());
        customerIdText.setDisable(true);

        // Initialize the ComboBox with the hours
        for(int i = 0; i < 24; i++)
        {
            if(i < 10)
            {
                arrivalHour.getItems().add("0" + i);
            }
            else
            {
                arrivalHour.getItems().add(i);
            }
        }

        // Initialize the ComboBox with the minutes
        for(int i = 0; i < 60; i++)
        {
            if(i < 10)
            {
                arrivalMin.getItems().add("0" + i);
            }
            else
            {
                arrivalMin.getItems().add(i);
            }
        }

        startDateOfMembership.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                // Disable all past dates
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        membershipOpt.getItems().add("Full Membership");
        membershipOpt.getItems().add("Routine Membership");
    }

    private boolean checkCarIdValidity()
    {
        String carId = carIdNumber.getText();
        String regex = "^[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(carId);
        return matcher.matches();
    }

    private boolean checkValidity()
    {
        if (carIdNumber.getText().isEmpty() || startDateOfMembership.getValue() == null
                || arrivalHour.getItems().isEmpty() || arrivalMin.getItems().isEmpty() || parkingLots.getItems().isEmpty())
        {
            sendAlert("Some fields have not been filled", "Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }

        if (!checkCarIdValidity())
        {
            sendAlert("Car ID is not valid", " Invalid Car ID", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    @Subscribe
    public void newResponse(MembershipMessage new_message)
    {
        switch (new_message.response_type) {
            case REGISTRATION_SUCCEEDED:
                sendAlert("Your Membership Number Is: \n" + new_message.newMembership.getMembershipId() +
                                "\nA charge of " + String.format("%.2f", new_message.newMembership.getMembershipsPrice()) + "₪ was made",
                        "Membership Id Number", Alert.AlertType.INFORMATION);
                break;

            case REGISTRATION_FAILED:
                sendAlert("YOU HAVE GOT ALREADY A MEMBERSHIP FOR THIS CAR!", "Error!", Alert.AlertType.WARNING);
                break;
        }
    }

    private Membership createMembership()
    {
        Membership membership = new Membership();
        LocalTime arrivalTime = LocalTime.MIN;

        membership.setCarId(Integer.parseInt(carIdNumber.getText()));
        membership.setCustomerId(customerIdText.getText());
        if(parkingLots.getValue() != null && arrivalHour.getValue() != null && arrivalMin.getValue() != null
            && !parkingLots.isDisable() && !arrivalHour.isDisable() && !arrivalMin.isDisable())
        {
            membership.setRoutineParkingLot(parkingLots.getValue());
            arrivalTime = LocalTime.of(Integer.parseInt(arrivalHour.getValue().toString()), Integer.parseInt(arrivalMin.getValue().toString()));
            membership.setRoutineLeavingHour(arrivalTime);
        }
        else
        {
            membership.setRoutineParkingLot("Haifa");
            membership.setRoutineLeavingHour(LocalTime.MIN);
        }

        membership.setMembershipType(membershipOpt.getValue());

        LocalDate startDate = startDateOfMembership.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, arrivalTime);
        membership.setStartDate(LocalDateTime.from(startDateTime));
        membership.setEndDate(startDateTime.plusDays(28)); //calculate the expiration date of the membership

        return membership;
    }

    @FXML
    void myOrders(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("MyOrders");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        LoginMessage msg = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,SimpleClient.getCurrent_user().getId());
        SimpleClient.getClient().sendToServer(msg);
    }
    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case LOGOUT_SUCCEED:
                Platform.runLater(()->
                {
                    try {
                        SimpleChatClient.setRoot("Login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }

    }
}
