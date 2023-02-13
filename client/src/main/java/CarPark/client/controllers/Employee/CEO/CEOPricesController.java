package CarPark.client.controllers.Employee.CEO;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Manager;
import CarPark.entities.ParkingLotWorker;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static CarPark.client.controllers.Controller.sendAlert;

public class CEOPricesController {
    @FXML
    private TableColumn<Price, String> typeCol;
    @FXML
    private TableColumn<Price, Integer> pricesCol;
    @FXML
    private TableColumn<Price, Integer> new_pricesCol;
    @FXML
    private TableColumn<Price,String> plCol;

    @FXML
    private TableView<Price> pricesTable;
    @FXML
    private TableColumn<Price, Void> approveCol;

    @FXML
    void initialize () throws IOException{
        EventBus.getDefault().register(this);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("parkingType"));
        pricesCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        new_pricesCol.setCellValueFactory(new PropertyValueFactory<>("newPrice"));
        plCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParkinglot().getName()));
        addButtonToTable();
        PricesMessage msg = new PricesMessage(Message.MessageType.REQUEST, PricesMessage.RequestType.GET_REQUESTS_TABLE);
        SimpleClient.getClient().sendToServer(msg);
    }

    //add cancel button to every order
    private void addButtonToTable() {
        approveCol = new TableColumn("");
        Callback<TableColumn<Price, Void>, TableCell<Price, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Price, Void> call(final TableColumn<Price, Void> param) {
                final TableCell<Price, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Approve");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Price price = getTableRow().getItem();
                            PricesMessage msg = new PricesMessage(Message.MessageType.REQUEST,
                                    PricesMessage.RequestType.APPROVE_PRICE, price);
                            try {
                                SimpleClient.getClient().sendToServer(msg);
                                pricesTable.getItems().remove(getTableRow().getItem()); //remove the order from table
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            btn.getStyleClass().add("style.css/mybutton");
                        }
                    }
                };
                return cell;
            }
        };

        approveCol.setCellFactory(cellFactory);
        pricesTable.getColumns().add(approveCol);
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("CEOPage");
    }

    @Subscribe
    public void newResponse(PricesMessage new_message) throws IOException {
        switch (new_message.response_type)
        {
            case SET_REQUESTS_TABLE:
                pricesTable.setItems(FXCollections.observableArrayList(new_message.priceList));
                break;
            case WAITING_FOR_APPROVAL:
            {
                sendAlert("New change request received."+ "\n" +"From:" + new_message.parkingLot,
                        "New Request", Alert.AlertType.INFORMATION);
                PricesMessage msg = new PricesMessage(Message.MessageType.REQUEST, PricesMessage.RequestType.GET_REQUESTS_TABLE);
                SimpleClient.getClient().sendToServer(msg);
                break;
            }
        }
    }

    public static class PricesManagerController {
        @FXML // fx:id="plPick"
        private Label parking_lot;
        @FXML
        private TableView<Price> pricesTable;
        @FXML
        private TableColumn<Price, String> typeCol;
        @FXML
        private TableColumn<Price, String> paymentCol;
        @FXML
        private TableColumn<Price, Integer> pricesCol;
        @FXML
        private TableColumn<Price, Integer> noCol;
        @FXML
        private TableColumn<Price, Integer> hopCol;


        @FXML
        private void loginPage() throws IOException {
            SimpleChatClient.setRoot("ParkingLotManagerPage");
        }


        @FXML
        private void Edit() {
            //case editing price
            pricesTable.setEditable(true);
            pricesCol.setCellFactory(TextFieldTableCell.<Price, Integer>forTableColumn(new IntegerStringConverter()));
            pricesCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Price, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Price, Integer> priceIntegerCellEditEvent) {//send request to server to set new price on DB
                    Price new_price = priceIntegerCellEditEvent.getRowValue();
                    new_price.setPrice(priceIntegerCellEditEvent.getNewValue());
                    PricesMessage message = new PricesMessage(Message.MessageType.REQUEST, PricesMessage.RequestType.EDIT_PRICE, new_price);

                    ParkingLotWorker current_manager = (ParkingLotWorker) SimpleClient.getCurrent_user();
                    message.parkingLot = current_manager.getParkinglot().getName();
                    try {
                        SimpleClient.getClient().sendToServer(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        @FXML
        void initialize() throws IOException {
            EventBus.getDefault().register(this);
            typeCol.setCellValueFactory(new PropertyValueFactory<>("parkingType"));
            paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
            pricesCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            noCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCars"));
            hopCol.setCellValueFactory(new PropertyValueFactory<>("hoursOfParking"));

            PricesMessage msg = new PricesMessage(Message.MessageType.REQUEST, PricesMessage.RequestType.GET_PRICES_TABLE);
            Manager current_manager = (Manager) SimpleClient.getCurrent_user();
            parking_lot.setText(current_manager.getParkinglot().getName());
            msg.parkingLot =  current_manager.getParkinglot().getName();
            SimpleClient.getClient().sendToServer(msg);
        }


        //initialize prices table from server
        @Subscribe
        public void newResponse(PricesMessage new_message) {
            switch (new_message.response_type) {
                case SET_PRICES_TABLE:
                    pricesTable.setItems(FXCollections.observableArrayList(new_message.priceList));
                    break;
                case WAITING_FOR_APPROVAL:
                    sendAlert("Price change request have been sent."+ "\n" +"Waiting For CEO To Approve.",
                            "Request Sent", Alert.AlertType.INFORMATION);
                    break;
            }
        }


    }
}
