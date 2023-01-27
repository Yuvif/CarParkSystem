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
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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

    @FXML // fx:id="btonCol"
    private TableColumn<Complaint, Void> btnCol; // Value injected by FXMLLoader

    @FXML // fx:id="complaintsTableView"
    private TableView<Complaint> complaintsTableView; // Value injected by FXMLLoader

    @FXML // fx:id="customerid"
    private TableColumn<Complaint, String> customerid; // Value injected by FXMLLoader

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
    void initialize() {
        EventBus.getDefault().register(this);
        assert subDate != null : "fx:id=\"subDate\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert desc != null : "fx:id=\"desc\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert plotID != null : "fx:id=\"plotID\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert customerid != null : "fx:id=\"customerid\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        //assert btnCol != null : "fx:id=\"btnCol\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";
        assert complaintsTableView != null : "fx:id=\"complaintsTableView\" was not injected: check your FXML file 'ComplaintInspectionTable.fxml'.";

        subDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        subDate.setStyle("-fx-alignment: CENTER");

        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        desc.setStyle("-fx-alignment: CENTER");

        plotID.setCellValueFactory(new PropertyValueFactory<>("parkinglot id"));
        plotID.setStyle("-fx-alignment: CENTER");

        customerid.setCellValueFactory(new PropertyValueFactory<>("customer id"));
        customerid.setStyle("-fx-alignment: CENTER");


//        status.setCellValueFactory(cellData -> {
//            String status = cellData.getValue();
//            return new ReadOnlyStringWrapper(status ? "Open" : "Closed");
//        });
//        status.setStyle("-fx-alignment:e CENTER");

        addButtonToTable();

        ComplaintMessage complaintMessage = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLAINTS);

        try {
            SimpleChatClient.client.sendToServer(complaintMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Adding button to each instance in the table.
     */
    private void addButtonToTable() {
        btnCol = new TableColumn("Inspect complaint");

        Callback<TableColumn<Complaint, Void>, TableCell<Complaint, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Complaint, Void> call(final TableColumn<Complaint, Void> param) {
                final TableCell<Complaint, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Action");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Complaint complaint = getTableView().getItems().get(getIndex());
                            if (SimpleClient.getCurrent_user() instanceof Employee)
                            {
                                //((Employee) SimpleClient.getCurrent_user()).setComplaintToInspect(complaint);
                                //ComplaintInspectionController
                            }

                            try {
                                System.out.println("try to set root");
                                SimpleChatClient.setRoot("ComplaintInspection");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            complaintsTableView.getItems().remove(getTableRow().getItem()); //remove the order from table
                        });
                        btn.setStyle("-fx-background-color:  #00acef");
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("complaint controller response back");
                //complaintsTableView.setItems(FXCollections.observableArrayList(new_message.complaints));

                complaintsTableView.setItems((ObservableList<Complaint>) new_message.complaints);
                int expired = 0;
                for (Complaint complaint : new_message.complaints) {
                    if ((new Date().getTime()) - (complaint.getDate().getTime()) > 86400000) {
                        complaint.setCompletedOnTime(false);
                        expired++;
                    }
                }
                //expireLabel.setText("You have " + new_message.complaints.size() + " complaints pending. Of which " + expired + " are expired!");
            }
        });
    }


}