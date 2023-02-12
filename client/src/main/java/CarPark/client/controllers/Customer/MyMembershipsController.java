package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.Membership;
import CarPark.entities.messages.MembershipMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MyMembershipsController {

    @FXML
    private Label userName;
    @FXML
    private TableView<Membership> membershipTableView;
    @FXML
    private TableColumn<Membership,Integer> membership_id;
    @FXML
    private TableColumn<Membership,Integer> car_id;
    @FXML
    private TableColumn<Membership,String> parking_lot;
    @FXML
    private TableColumn<Membership, LocalTime> leaving_hour;
    @FXML
    private TableColumn<Membership, LocalDate> start_date;
    @FXML
    private TableColumn<Membership,LocalDate> end_date;
    @FXML
    private TableColumn<Membership,String> type;


    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        membership_id.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        car_id.setCellValueFactory(new PropertyValueFactory<>("carId"));
        parking_lot.setCellValueFactory(new PropertyValueFactory<>("routineParkingLot"));
        leaving_hour.setCellValueFactory(new PropertyValueFactory<>("routineLeavingHour"));
        start_date.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        end_date.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        type.setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        MembershipMessage msg = new MembershipMessage(Message.MessageType.REQUEST, MembershipMessage.RequestType.GET_MY_MEMBERSHIPS,
                (Customer)SimpleClient.getCurrent_user());
        SimpleClient.getClient().sendToServer(msg);
    }

    @Subscribe
    public void newResponse(MembershipMessage message) {
        switch (message.response_type) {
            case SEND_TABLE:
                membershipTableView.setItems(FXCollections.observableArrayList(message.memberships));
                break;
        }
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

}
