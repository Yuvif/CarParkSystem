
package CarPark.client.controllers;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.event.MouseEvent;
import java.io.IOException;


public class EditParkingSlotsController {

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

//    @FXML // fx:id="plTable"
//    private static TableView<ParkingSlot> plTable; // Value injected by FXMLLoader

    @FXML
    private TableView<ParkingSlot> plTable;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        idCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot,Integer>("id"));
        currStaCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, ParkingSlot.Status>("SpotStatus"));
        editStaCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, ParkingSlot.Status>("SpotStatus"));
        plPick.getItems().add("Set ParkingLot");
        //ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
        ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_ALL_PARKING_LOTS);
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void displayParkingLot(ActionEvent event) throws IOException {
        if (checkEmpty())
        {
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

        if(plPick.getValue().equals("Set ParkingLot")){
           // sendAlert("Please select a parking lot", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
    ///////////////////////////////////////////
    //Setting the Parkinglots from database into the choosebox
//    private void getParkingLots() {
//        this.parkinglots = SimpleChatClient.client.getParkinglots();
//        plPick.getItems().add("Set ParkingLot");
//        plPick.setValue("Set ParkingLot");
//        for (Parkinglot s : parkinglots)
//            plPick.getItems().add(s.getId());
//    }
//    @FXML
//    void displayParkingLot(ActionEvent event) {
//        if (checkEmpty()) {
//            List<Object> msg = new LinkedList<>();
//            msg.add("#PULL_SELECTED_PARKINGSLOTS");
//            msg.add(plPick.getValue().toString());
//            try {
//                SimpleChatClient.client.sendToServer(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void pullParkingslots(ObservableList<ParkingSlot> parkingslots) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                plTable.setItems(parkingslots);
//            }
//        });
//    }

}