package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class CustomerPageController{
    @FXML
    private Label userName;
    @FXML
    private Label balance;
    @FXML
    private Button newMember;

    @FXML
    public void registerAsMember(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("RegisterAsMember");
    }

    @FXML
    public void addComplaint(){}

    @FXML
    public void myOrders(){}

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        Customer current_customer = (Customer)SimpleClient.getCurrent_user();
        PricesMessage msg = new PricesMessage
                (Message.MessageType.REQUEST, PricesMessage.RequestType.GET_CURRENT_BALANCE,current_customer);
        SimpleClient.getClient().sendToServer(msg);
        userName.setText("Hello, " + current_customer.getFirstName());
    }

    @Subscribe
    public void newResponse(PricesMessage new_message){
        switch (new_message.response_type) {
            case SET_CURRENT_BALANCE:
                Platform.runLater(()->balance.setText(String.valueOf(new_message.price)));
        }
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
    void checkOut(ActionEvent event)throws IOException{
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("CheckOutCustomer");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void checkIn(ActionEvent event)throws IOException{
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("CheckInCustomer");
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

