package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.controllers.Controller;
import CarPark.entities.Complaint;
import CarPark.entities.Customer;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Date;

public class ComplaintSubmissionController extends Controller {

    @FXML
    private Label userName;

    @FXML // fx:id="complaintDesc"
    private TextField complaintDesc; // Value injected by FXMLLoader

    @FXML // fx:id="sendComplaintBtn"
    private Button sendComplaintBtn; // Value injected by FXMLLoader

    @FXML
    private TextField customerIdT;

    @FXML
    private ComboBox <String> plPick;




    @FXML
    void sendComplaint(ActionEvent event) {
        if (checkEmpty()) {
//            create a complaint with the description
            Complaint complaint = new Complaint(new Date(), complaintDesc.getText(), customerIdT.getText());
//            create a list with the complaint
//            send the complaint to the server
            ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.CREATE_NEW_COMPLAINT, complaint,
                    (Customer) SimpleClient.getCurrent_user());
            try {
                SimpleChatClient.client.sendToServer(complaintMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() throws IOException
    {
        EventBus.getDefault().register(this);
        customerIdT.setText(SimpleClient.getCurrent_user().getId().toString());
        plPick.getItems().add("Haifa");
        plPick.getItems().add("Tel Aviv");
        plPick.getItems().add("Jerusalem");
        plPick.getItems().add("Be'er Sheva");
        plPick.getItems().add("Eilat");
    }


    @Subscribe
    public void newResponse(ComplaintMessage new_message) {
        if (new_message.response_type == ComplaintMessage.ResponseType.COMPLAINT_SUBMITTED) {
            Platform.runLater(() -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Complaint submitted");
                    alert.show();
                    PauseTransition pause = new PauseTransition(Duration.seconds(5));
                    pause.setOnFinished((e -> {
                        alert.close();
                    }));
                    pause.play();
                });
            });
        }
    }

    private boolean checkEmpty() {
        if (complaintDesc.getText().isEmpty()) {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
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
                SimpleChatClient.setRoot("OrdersTable");
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
    void myComplaints(ActionEvent event) throws IOException{
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("MyComplaints");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}