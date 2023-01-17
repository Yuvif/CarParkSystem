
package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import CarPark.entities.messages.ParkingSlotsMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.event.MouseEvent;
import java.io.IOException;


public class EditParkingSlotsController extends  Controller {

    @FXML // fx:id="currStaCol"
    private TableColumn<ParkingSlot, ParkingSlot.Status> currStaCol; // Value injected by FXMLLoader

    @FXML // fx:id="editStaCol"
    private TableColumn<ParkingSlot, ParkingSlot.Status> editStaCol; // Value injected by FXMLLoader

    @FXML // fx:id="idCol"
    private TableColumn<ParkingSlot, Integer> idCol; // Value injected by FXMLLoader

    @FXML // fx:id="displayB"
    private Button displayB; // Value injected by FXMLLoader

    @FXML // fx:id="plPick"
    private ComboBox<String> plPick; // Value injected by FXMLLoader

    @FXML
    private TableView<ParkingSlot> plTable;
    private TableColumn btnCol;


    @FXML
    void initialize() throws IOException {
        assert plPick != null : "fx:id=\"plPick\" was not injected: check your FXML file 'Login.fxml'.";

        EventBus.getDefault().register(this);
        idCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, Integer>("id"));
        currStaCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, ParkingSlot.Status>("SpotStatus"));
        editStaCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, ParkingSlot.Status>("SpotStatus"));
        //this.parkinglots = SimpleChatClient.client.getParkinglots();
        plPick.getItems().add("Set ParkingLot");
        //addButtonToTable();

        //ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
        ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_ALL_PARKING_LOTS);
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void displayParkingLot(ActionEvent event) throws IOException {
        if (checkEmpty()) {
            //--will be an auto choosing of the specific parkinglot of the P-l worker (after login):
            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
            //temporary - simply choose parkinglot from the combo box like a customer-service general employee:
            ParkingSlotsMessage msg2 = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_SELECTED_PARKING_SLOTS, plPick.getValue());
            SimpleClient.getClient().sendToServer(msg2);
        }
    }

    //update table from server
//    @Subscribe
//    public void newResponse(ParkingListMessage new_message) {
//        System.out.println("we got controller back from p-List message");
//        switch (new_message.response_type) {
//            case SET_ALL_PARKING_LOTS:
//                for (Parkinglot s : new_message.parkingList)
//                    plPick.getItems().add(s.getId());
//                break;
//        }
//    }
    @Subscribe
    public void newResponse(ParkingSlotsMessage new_message) {
        System.out.println("we got controller back from p-slot message");
        switch (new_message.response_type) {
            case SET_ALL_PARKING_LOTS:
                for (Parkinglot s : new_message.parkingList)
                    plPick.getItems().add(s.getId());
                break;
            case SET_PARKING_SLOTS:
                System.out.println("we got change table");
                System.out.println(new_message.parkingSlots.get(0).getParkinglot().getId());
                //plTable.setItems((ObservableList<ParkingSlot>) new_message.parkingSlots);
                plTable.setItems(FXCollections.observableArrayList(new_message.parkingSlots));
                break;
            case PARKING_SLOT_EDITED:
                break;
        }
    }

    private boolean checkEmpty() {

        if (plPick.getValue().equals("Set ParkingLot")) {
            // sendAlert("Please select a parking lot", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    /**
     * Adding button to each instance in the table.
     */
//    private void addButtonToTable() {
//        btnCol = new TableColumn("Edit status");
//
//        Callback<TableColumn<ParkingSlot, Void>, TableCell<ParkingSlot, Void>> cellFactory = new Callback<>() {
//            @Override
//            public TableCell<ParkingSlot, Void> call(final TableColumn<ParkingSlot, Void> param) {
//                final TableCell<ParkingSlot, Void> cell = new TableCell<>() {
//
//                    private final Button btn = new Button("Action");
//
//                    {
//                        btn.setOnAction((ActionEvent event) -> {
//                            ParkingSlot parkingSlot = getTableView().getItems().get(getIndex());
////                            goToComplaintInspection(complaint);
//                        });
//                        btn.setStyle("-fx-background-color:  #c6acef");
//                        btn.setText("Inspect");
//                    }
//
//                    @Override
//                    public void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(btn);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//
//        btnCol.setCellFactory(cellFactory);
//        btnCol.setStyle("-fx-alignment: CENTER");
//        plTable.getColumns().add(btnCol);
//
//    }
}