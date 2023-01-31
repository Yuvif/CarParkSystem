package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.messages.CheckOutMessage;
import CarPark.entities.messages.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static CarPark.client.controllers.Controller.sendAlert;

public class CheckOutGuestController {

    @FXML
    private TextField carNumber;

    @FXML
    private TextField userId;

    @FXML
    void goBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("Login");
    }

    @FXML
    void submit(ActionEvent event)
    {
        if (checkValidity())
        {
            CheckOutMessage checkOutMessage = new CheckOutMessage(Message.MessageType.REQUEST, CheckOutMessage.RequestType.CHECK_ME_OUT_GUEST,
                    Long.parseLong(userId.getText()), Integer.parseInt(carNumber.getText()), false);
            try {
                SimpleClient.getClient().sendToServer(checkOutMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void newResponse(CheckOutMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case CHECKED_OUT_GUEST -> {
                //if the checked out user is a guest
                sendAlert("You were checked out successfully! \nA charge of "+ String.format("%.2f", new_message.payment)
                                +" ₪ was made.\nThank You! Goodbye!" ,
                        "Check Out", Alert.AlertType.INFORMATION);
            }
        }
    }

    private boolean checkValidity() {
        if (!checkIdValidity(userId.getText())) {
            sendAlert("ID is not valid", " Invalid ID", Alert.AlertType.WARNING);
            return false;
        }

        if (!checkCarIdValidity(carNumber.getText())) {
            sendAlert("Car ID is not valid", " Invalid Car ID", Alert.AlertType.WARNING);
            return false;
        }

        if (carNumber.getText().isEmpty() || userId.getText().isEmpty()) {
            sendAlert("Please fill all the fields", "warning", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private boolean checkIdValidity(String id) {
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        if (matcher.matches())
            return true;
        return false;
    }

    private boolean checkCarIdValidity(String carId) {
        String regex = "^[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(carId);
        return matcher.matches();
    }

}
