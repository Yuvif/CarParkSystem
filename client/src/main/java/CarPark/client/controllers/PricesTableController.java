package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PricesMessage;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

public class PricesTableController {

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
    private void GoBack() throws IOException {
        SimpleChatClient.setRoot("MainScreen");
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
        SimpleClient.getClient().sendToServer(msg);
    }


    //initialize prices table from server
    @Subscribe
    public void newResponse(PricesMessage new_message) {
        switch (new_message.response_type) {
            case SET_PRICES_TABLE:
                pricesTable.setItems(FXCollections.observableArrayList(new_message.priceList));
                break;
            case PRICE_EDITED:
                break;
        }
    }


}

