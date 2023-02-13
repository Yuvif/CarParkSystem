package CarPark.client.controllers.Employee.Manager;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Manager;
import CarPark.entities.ParkingLotWorker;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static CarPark.client.controllers.Controller.sendAlert;

public class PricesManagerController {
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
                PricesMessage message = new
                        PricesMessage(Message.MessageType.REQUEST, PricesMessage.RequestType.EDIT_PRICE, new_price);
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

