package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.MessageEvent;
import CarPark.client.events.ParkingListMessageEvent;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static javafx.collections.FXCollections.observableArrayList;

public class ParkingListController extends Controller {


    @FXML
    private TableView<Parkinglot> parkingTable;
    @FXML
    private TableColumn<Parkinglot, String> nameCol;
    @FXML
    private TableColumn<Parkinglot, Integer> parksPerRowCol;
    @FXML
    private TableColumn<Parkinglot, Integer> totalCol;


    @FXML
    private void GoBack() throws IOException  {

        SimpleChatClient.setRoot("MainScreen");
    }

    @FXML
    void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<Parkinglot, String>("id"));
        parksPerRowCol.setCellValueFactory(new PropertyValueFactory<Parkinglot, Integer>("parksPerRow"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Parkinglot, Integer>("totalParkingLots"));
        ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void setParkingTable(ParkingListMessageEvent event) {
        System.out.println("succeed");
                parkingTable.setItems((ObservableList<Parkinglot>)event.table);
    }



//    @Subscribe
//    public void setParkingTable(MessageEvent event) {
//        if (inStage) {
//            p_l = (ArrayList<Parkinglot>) event.getMessage().getTable();
//            parkingTable.setItems(FXCollections.observableArrayList(p_l));
//      }
//}


}