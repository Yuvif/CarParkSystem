package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.messages.CheckOutMessage;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static CarPark.client.controllers.Controller.sendAlert;

public class CheckOutCustomerController {

    @FXML
    private TextField carNumber;

    @FXML
    private TextField userId;

    @FXML
    private Label userName;

    @FXML
    void submit(ActionEvent event)
    {
        if (checkValidity())
        {
            CheckOutMessage checkOutMessage = new CheckOutMessage(Message.MessageType.REQUEST, CheckOutMessage.RequestType.CHECK_ME_OUT,
                    (Customer) SimpleClient.getCurrent_user(), carNumber.getText(), 0.0 , false);
            checkOutMessage.userId = checkOutMessage.current_customer.getId();

            try {
                SimpleClient.getClient().sendToServer(checkOutMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void newResponse(CheckOutMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case CHECKED_OUT -> {
                //if the checked out user is a guest
                if(new_message.payment != 0.0)
                {
                    sendAlert("You were checked out successfully!" +
                                    "\nThank You! Goodbye!" ,
                            "Check Out", Alert.AlertType.INFORMATION);
                }
                else if(new_message.hasAnOrder)
                {
                    sendAlert("You were checked out successfully!" +
                                    "\nA charge of " + String.format("%.2f",new_message.payment) + " ₪ was made. " +
                                    "\n Thank You! Goodbye!" ,
                            "Check Out", Alert.AlertType.INFORMATION);
                }
                else
                {
                    sendAlert("You were checked out successfully!" +
                                    "\nA charge of " + String.format("%.2f",new_message.payment) + " ₪ was made. " +
                                    "\n Thank You! Goodbye!" ,
                            "Check Out", Alert.AlertType.INFORMATION);
                }
            }
        }
    }

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        userId.setText(SimpleClient.getCurrent_user().getId());
    }

    private boolean checkCarIdValidity(String carId) {
        String regex = "^[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(carId);
        return matcher.matches();
    }

    private boolean checkValidity()
    {
        if (!checkCarIdValidity(carNumber.getText()) || carNumber.getText().isEmpty()) {
            sendAlert("Car ID is not valid", " Invalid Car ID", Alert.AlertType.WARNING);
            return false;
        }

        return true;
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
    private void newMembership(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("RegisterAsMember");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
    void logout(ActionEvent event) throws IOException {
        LoginMessage msg = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT,SimpleClient.getCurrent_user().getId());
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void mainMenu(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("CustomerPage");
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
