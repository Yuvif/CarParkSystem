/**
 * Sample Skeleton for 'ComplaintInspectionTable.fxml' Controller Class
 */

package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Complaint;
import CarPark.entities.Employee;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import antlr.ASTNULLType;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class ComplaintInspectionTableController extends Controller {


    @FXML
    private Button backBtn;

    @FXML
    private Label expireLabel;

    @FXML // fx:id="complaintsTableView"
    private TableView<Complaint> complaintsTableView; // Value injected by FXMLLoader

    @FXML // fx:id="customerid"
    private TableColumn<Complaint, Long> customerid; // Value injected by FXMLLoader

    @FXML // fx:id="desc"
    private TableColumn<Complaint, String> desc; // Value injected by FXMLLoader

    @FXML // fx:id="plotID"
    private TableColumn<Complaint, String> plotID; // Value injected by FXMLLoader

    @FXML // fx:id="subDate"
    private TableColumn<Complaint, Date> subDate; // Value injected by FXMLLoader

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;



    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        EventBus.getDefault().register(this);

        assert subDate != null : "fx:id=\"subDate\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert desc != null : "fx:id=\"desc\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert plotID != null : "fx:id=\"plotID\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert customerid != null : "fx:id=\"customerid\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        //assert btnCol != null : "fx:id=\"btnCol\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert complaintsTableView != null : "fx:id=\"complaintsTableView\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";


        subDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        subDate.setStyle("-fx-alignment: CENTER");

        //desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        desc.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCompText()));
        desc.setStyle("-fx-alignment: CENTER");

        //plotID.setCellValueFactory(new PropertyValueFactory<>("parkinglot id"));
        plotID.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkinglot().getName()));
        plotID.setStyle("-fx-alignment: CENTER");

       // customerid.setCellValueFactory(new PropertyValueFactory<>("customer id"));

        customerid.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getCustomer().getId()).asObject());
        customerid.setStyle("-fx-alignment: CENTER");


//        status.setCellValueFactory(cellData -> {
//            String status = cellData.getValue();
//            return new ReadOnlyStringWrapper(status ? "Open" : "Closed");
//        });
//        status.setStyle("-fx-alignment:e CENTER");

        ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLAINTS);
        try {
            SimpleChatClient.client.sendToServer(complaintMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        addButtonToTable();

    }
    /**
     * Adding button to each instance in the table.
     */
    private void addButtonToTable() {
        TableColumn btnCol = new TableColumn("Inspect complaint");

        Callback<TableColumn<Complaint, Void>, TableCell<Complaint, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Complaint, Void> call(final TableColumn<Complaint, Void> param) {
                final TableCell<Complaint, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Action");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Complaint complaint = getTableView().getItems().get(getIndex());
                            //send message to the server with the complaint

                            //ComplaintMessage msg = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_OPEN_COMPLAINT, complaint);
//                            try {
//                                SimpleClient.getClient().sendToServer(msg);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            if (SimpleClient.getCurrent_user() instanceof Employee)
                                SimpleClient.getCurrent_user().setComplaint2Inspect(complaint);

                            try {
                                SimpleChatClient.setRoot("ComplaintInspection");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                           // complaintsTableView.getItems().remove(getTableRow().getItem()); //remove the order from table


                        });
                        btn.setStyle("-fx-background-color:  #1aaf71");
                        btn.setText("Inspect");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        btnCol.setCellFactory(cellFactory);
        btnCol.setStyle("-fx-alignment: CENTER");
        complaintsTableView.getColumns().add(btnCol);
    }

    /**
     * Changing the screen to complaint inspection relative to the complaint chosen.
     * @param complaint
     */
//    private void goToComplaintInspection(Complaint complaint) {
//        ComplaintInspectionController controller = (ComplaintInspectionController)
//                this.getSkeleton().changeCenter("ComplaintInspection");
//        controller.setComplaint(complaint);
//    }
    /**
     * Displaying all the complaints in the table.
     *
     */
    @Subscribe
    public void newResponse(ComplaintMessage new_message)  {
        Platform.runLater(() -> {
            if (new_message.response_type == ComplaintMessage.ResponseType.SET_ALL_COMPLAINTS)
            {
                List<Complaint> complaints = new_message.complaints;
                complaints.removeIf(Complaint::getAppStatus);

                complaintsTableView.setItems(FXCollections.observableArrayList(complaints));
                int expired = 0;
                for (Complaint complaint : new_message.complaints) {
                    if ((new Date().getTime()) - (complaint.getDate().getTime()) > 86400000) {
                        complaint.setCompletedOnTime(false);
                        expired++;
                    }
                }

                expireLabel.setText("You have " + new_message.complaints.size() + " complaints pending. Of which " + expired + " are expired!");
            }
        });
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("EmployeePage");
    }


}