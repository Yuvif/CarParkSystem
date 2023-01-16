package CarPark.client.controllers;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Membership;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.RegisterMessage;
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

public class RegisterController {//Daniel need to change name to new member reg, also in server...

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
            RegisterMessage msg = new RegisterMessage(Message.MessageType.REQUEST, RegisterMessage.RequestType.REGISTER, membership);
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
        parkingLots.getItems().add("Haifa");
        parkingLots.getItems().add("Tel Aviv");
        parkingLots.getItems().add("Jerusalem");
        parkingLots.getItems().add("Be'er Sheva");
        parkingLots.getItems().add("Eilat");

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

    private boolean checkIdValidity()
    {
        String id = customerIdText.getText();
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
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
        if (customerIdText.getText().isEmpty() || carIdNumber.getText().isEmpty() || startDateOfMembership.getValue() == null
                || arrivalHour.getItems().isEmpty() || arrivalMin.getItems().isEmpty() || parkingLots.getItems().isEmpty())
        {
            sendAlert("Some fields have not been filled", "Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }

        if (!checkIdValidity())
        {
            sendAlert("ID is not valid", " Invalid ID", Alert.AlertType.WARNING);
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
    public void newResponse(RegisterMessage new_message)
    {
        switch (new_message.response_type) {
            case REGISTRATION_SUCCEEDED:
                sendAlert("Your Membership Number Is: " + new_message.newMembership.getMembershipId(),
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
        membership.setCustomerId(Long.parseLong(customerIdText.getText()));
        if(parkingLots.getValue() != null && arrivalHour.getValue() != null && arrivalMin.getValue() != null)
        {
            membership.setRoutineParkingLot(parkingLots.getValue().toString());
            arrivalTime = LocalTime.of(Integer.parseInt(arrivalHour.getValue().toString()), Integer.parseInt(arrivalMin.getValue().toString()));
            membership.setRoutineLeavingHour(arrivalTime);
        }
        else
        {
            membership.setRoutineParkingLot("NULL");
            membership.setRoutineLeavingHour(LocalTime.MIN);
        }

        membership.setMembershipType(membershipOpt.getValue());

        LocalDate startDate = startDateOfMembership.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, arrivalTime);
        membership.setStartDate(LocalDate.from(startDateTime));
        membership.setEndDate(startDateOfMembership.getValue().plusDays(28)); //calculate the expiration date of the membership

        return membership;
    }

}
