package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Complaint;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ComplaintInspectionController extends Controller {

    // Pattern for the text field not to consist more then five numbers and to consist only numbers.
    Pattern pattern1 = Pattern.compile(".{0,5}");
    TextFormatter<String> formatter1 = new TextFormatter<String>(change -> {
        change.setText(change.getText().replaceAll("[^0-9]", ""));
        return pattern1.matcher(change.getControlNewText()).matches() ? change : null;
    });
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="compensationCheckbox"
    private CheckBox compensationCheckbox; // Value injected by FXMLLoader
    @FXML // fx:id="compensationField"
    private TextField compensationField; // Value injected by FXMLLoader
    @FXML // fx:id="complainerName"
    private TextArea complainerName; // Value injected by FXMLLoader
    @FXML // fx:id="complaintText"
    private TextArea complaintText; // Value injected by FXMLLoader
    @FXML // fx:id="submitBtn"
    private Button submitBtn; // Value injected by FXMLLoader
    @FXML // fx:id="store"
    private Label parkinglot; // Value injected by FXMLLoader
    private Complaint complaint;

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    /**
     * Enabling and disabling the refund field.
     * @param event
     */
    @FXML
    void compensationMode(ActionEvent event) {
        compensationField.setEditable(compensationCheckbox.isSelected());
        if (compensationCheckbox.isSelected())
            compensationField.setDisable(false);
        else {
            compensationField.setDisable(true);
            compensationField.setText("");
        }
    }

    /**
     * Closing the complaint and asking the server to refund accordingly.
     * @param event
     */
    @FXML
    void submitInspection(ActionEvent event) throws IOException {
        Date d = new Date();
        Date minusDay = new Date(d.getTime() - Duration.ofDays(1).toMillis());
        Boolean b = complaint.getDate().getTime() - minusDay.getTime() >= 0;
        if (!b) {
            Controller.sendAlert("Complaint completed too late", "Late Inspection", Alert.AlertType.WARNING);
        }
        int amount = 0;
        if (compensationCheckbox.isSelected()) {
            if (compensationField.getText().isEmpty()) {
                sendAlert("No amount entered", "No Refund Given", Alert.AlertType.WARNING);
                return;
            }
            amount = Integer.parseInt(compensationField.getText());
        }
        ComplaintMessage msg = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.COMPENSATE_COMPLAINT, complaint, amount);
        SimpleClient.getClient().sendToServer(msg);
        complaint.setStatus(true);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Complaint filed, Sending back to table");
            alert.show();
            PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2.5));
            pause.setOnFinished((e -> {
                alert.close();
                try {
                    SimpleChatClient.setRoot("ComplaintInspectionTable");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }));
            pause.play();
        });

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert compensationCheckbox != null : "fx:id=\"compensationCheckbox\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        assert compensationField != null : "fx:id=\"compensationField\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        assert complainerName != null : "fx:id=\"complainerName\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        assert complaintText != null : "fx:id=\"complaintText\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        assert submitBtn != null : "fx:id=\"submitBtn\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        assert parkinglot != null : "fx:id=\"store\" was not injected: check your FXML file 'ComplaintInspection.fxml'.";
        Platform.runLater(() -> {
            complaintText.setText(complaint.getCompText());
            //complainerName.setText(complaint.getCustomer().getName());
            complaintText.setEditable(false);
            complainerName.setEditable(false);
            compensationField.setDisable(true);
            compensationField.setTextFormatter(formatter1);
            parkinglot.setText(complaint.getParkinglot().getId());

        });
    }

}