package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jvnet.staxex.util.DummyLocation;

import javax.security.auth.callback.Callback;
import java.awt.*;
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

    private static ArrayList<Price> p_t;
    private static int msgId = 0;
    private static boolean inStage;

    @FXML
    private void GoBack() throws IOException {
        inStage = false;
        SimpleChatClient.setRoot("MainScreen");
    }

    @FXML
    private void Edit() {
        //case editing price
        pricesTable.setEditable(true);
        pricesCol.setCellFactory(TextFieldTableCell.<Price, Integer>forTableColumn(new IntegerStringConverter()));
        pricesCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Price, Integer>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Price, Integer> priceIntegerCellEditEvent) {//send request to server to set new price on DB
                Price new_price = priceIntegerCellEditEvent.getRowValue();
                new_price.setPrice(priceIntegerCellEditEvent.getNewValue());
                if (new_price.getPrice()>=0) {
                    Message message = new Message(new_price, "edit price");
                    try {
                        SimpleClient.getClient().sendToServer(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @FXML
    void initialize() throws IOException {
        inStage = true;
        EventBus.getDefault().register(this);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        pricesCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        noCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCars"));
        hopCol.setCellValueFactory(new PropertyValueFactory<>("hoursOfParking"));
        if (msgId > 0){
            pricesTable.setItems(FXCollections.observableArrayList(p_t));
        }
        else {
            Message msg = new Message(msgId++, "get prices table");
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
        try {
            Message message = new Message(msgId, "send Submitters IDs");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //initialize prices table from server
    @Subscribe
    public void setPricesTable(MessageEvent event) {
        if (inStage) {
            if (event.getMessage().getMessage().equals("price updated"))
            {
            }
            else {
                p_t = (ArrayList<Price>) event.getMessage().getTable();
                pricesTable.setItems(FXCollections.observableArrayList(p_t));
            }
        }
    }


}

