
package CarPark.client.controllers;

import CarPark.client.SimpleClient;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingSlotsMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


public class EditParkingSlotsController extends  Controller {

    @FXML // fx:id="currStaCol"
    private TableColumn<ParkingSlot, ParkingSlot.Status> currStaCol; // Value injected by FXMLLoader

//    @FXML // fx:id="editStaCol"
//    private TableColumn<ParkingSlot, ParkingSlot.Status> editStaCol; // Value injected by FXMLLoader

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
        assert plPick != null : "fx:id=\"plPick\" was not injected: check your FXML file 'EditParkingSlots.fxml'.";

        EventBus.getDefault().register(this);
        idCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, Integer>("id"));
        //checkedInId.setCellValueFactory(new PropertyValueFactory<ParkingSlot, CheckedIn>("SpotStatus"));
        currStaCol.setCellValueFactory(new PropertyValueFactory<ParkingSlot, ParkingSlot.Status>("SpotStatus"));
        addChoiseBoxToTable();

        plPick.getItems().add("Set ParkingLot");
        //ObservableList<ParkingSlot> data;
        //plTable.setItems(data);

        ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_ALL_PARKING_LOTS);
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void displayParkingLot(ActionEvent event) throws IOException {
        if (checkEmpty()) {
            //--will be an auto choosing of the specific parkinglot of the P-l worker (after login):
            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
            //SimpleClient.getCurrent_user().getParkinglot()
            //temporary - simply choose parkinglot from the combo box like a customer-service general employee:
            ParkingSlotsMessage msg2 = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_SELECTED_PARKING_SLOTS, plPick.getValue());
            SimpleClient.getClient().sendToServer(msg2);
        }
    }

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
            sendAlert("Please select a parking lot", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void addChoiseBoxToTable() {
        TableColumn<ParkingSlot, String> column = new TableColumn<>("Column Name");
        column.setCellFactory(ComboBoxTableCell.forTableColumn("Option 1", "Option 2", "Option 3"));
        plTable.getColumns().add(column);

        column.setCellFactory(col -> new ComboBoxTableCell<ParkingSlot, String>(
                String.valueOf(new Callback<TableColumn<ParkingSlot, String>, TableCell<ParkingSlot, String>>() {
                    public TableCell<ParkingSlot, String> call(TableColumn<ParkingSlot, String> param) {
//                        return new ComboBoxTableCell<ParkingSlot, String>() {

                            final ComboBoxTableCell<ParkingSlot, String> comboCell = new ComboBoxTableCell<ParkingSlot,String> () {
                                private final ComboBoxTableCell cBox = new ComboBoxTableCell("Edit status");
                                private final ComboBox c = new ComboBox();

                                //cBox.getTableRow().getItem().
                           //@Override
                           public void updateItem(ParkingSlot parkingSlot, boolean empty) {
                               super.updateItem(String.valueOf(parkingSlot.getSpotStatus()), empty);
                               if (!empty) {
                                   // set items for the ComboBox here
                                   // for example, you can use getIndex() to get the row index
                                   // and use it to retrieve the data from your model
                                   getItems().setAll("Option 1", "Option 2", "Option 3");
                                   cBox.getItems().add(ParkingSlot.Status.RESERVED);
                                   cBox.getItems().add(ParkingSlot.Status.RESTRICTED);
                                   cBox.getItems().add(ParkingSlot.Status.EMPTY);

                                   cBox.setContentDisplay(ContentDisplay.valueOf(String.valueOf(((ParkingSlot) cBox.getTableRow().getItem()).getSpotStatus())));
                                   cBox.setItem(((ParkingSlot) cBox.getTableRow().getItem()).getSpotStatus());

                                   c.setOnAction(event -> {
                                       // handle the selection here
                                       // for example, you can use getTableView().getItems().get(getIndex())
                                       // to get the data of the selected row
                                       String selectedValue = cBox.getText().toString(); //textFill
                                       ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.SET_PARKING_SLOT_STATUS, selectedValue);
                                       try {
                                           SimpleClient.getClient().sendToServer(msg);
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }

                                   });
//                                   cBox.setOnAction(event -> {
//                                       // handle the selection here
//                                       // for example, you can use getTableView().getItems().get(getIndex())
//                                       // to get the data of the selected row
//                                       String selectedValue = getValue();
//                                       // your code here
//                                   });
                               }

                           }


                    };
                        return comboCell;

    }

    /**
     * Adding button to each instance in the table.
     */
//    private void addChoiseBoxToTable() {
//        btnCol = new TableColumn("");
//
//        Callback<TableColumn<ParkingSlot, Void>, TableCell<ParkingSlot, Void>> cellFactory = new Callback<>() {
//            @Override
//            public TableCell<ParkingSlot, Void> call(final TableColumn<ParkingSlot, Void> param) {
//                final ComboBoxTableCell<ParkingSlot.Status, String> comboCell = new ComboBoxTableCell<ParkingSlot,ParkingSlot.Status> () {
//                    //cBox.setComboBoxEditable(true);
//                    private final ComboBoxTableCell cBox = new ComboBoxTableCell("Edit status");
//                    {
//                        ParkingSlot p = (ParkingSlot) cBox.getTableRow().getItem();
//                        ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST,
//                                ParkingSlotsMessage.RequestType.SET_PARKING_SLOT_STATUS, p, (ParkingSlot.Status) cBox.getTableView());
//                        try {
//                            SimpleClient.getClient().sendToServer(msg);
//                            cBox.updateSelected(true);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                };
//
//                final TableCell<ParkingSlot, Void> cell = new TableCell<>() {
//
//                    private final Button btn = new Button("Cancel");
//                    {
//                        btn.setOnAction((ActionEvent event) -> {
//                            ParkingSlot parkingSlot = getTableRow().getItem();
//                            ParkingSlotsMessage msg = new ParkingSlotsMessage(Message.MessageType.REQUEST,
//                                    ParkingSlotsMessage.RequestType.SET_PARKING_SLOT_STATUS, );
//                            try {
//                                SimpleClient.getClient().sendToServer(msg);
//                                plTable.getItems().remove(getTableRow().getItem()); //remove the order from table
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                    }
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
//        btnCol.setCellFactory(cellFactory);
//        btnCol.setStyle("-fx-alignment: CENTER");
//        plTable.getColumns().add(btnCol);
//
//    }
})));}}