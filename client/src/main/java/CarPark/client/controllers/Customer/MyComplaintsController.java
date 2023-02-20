package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Complaint;
import CarPark.entities.Customer;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;

public class MyComplaintsController {

    @FXML
    private Label userName;
    @FXML
    private TableView<Complaint> complaintsTableView;
    @FXML
    private TableColumn<Complaint,Integer> complaint_id;
    @FXML
    private TableColumn<Complaint,LocalDate> subDate;
    @FXML
    private TableColumn<Complaint,Boolean> status;



    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        complaint_id.setCellValueFactory(new PropertyValueFactory<>("complaintId"));
        subDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        status.setCellValueFactory(new PropertyValueFactory<Complaint, Boolean>("appStatus"));
        ComplaintMessage msg = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_MY_COMPLAINTS,
                (Customer)SimpleClient.getCurrent_user());
        SimpleClient.getClient().sendToServer(msg);
    }


    @Subscribe
    public void newResponse(ComplaintMessage message) {
        switch (message.response_type) {
            case SET_MY_COMPLAINTS:
                complaintsTableView.setItems(FXCollections.observableArrayList(message.complaints));
                status.setCellFactory(col -> new TableCell<Complaint, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item ? "Handled" : "Waiting");
                        }
                    }
                });
                break;
        }
    }


    @FXML
    private void myMemberships(ActionEvent event) {
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
