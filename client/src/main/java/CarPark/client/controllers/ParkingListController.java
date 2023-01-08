package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.events.NewSubscriberEvent;
import CarPark.client.events.ParkingListEvent;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class ParkingListController{


    @FXML
    private TableView<Parkinglot> parkingTable;
    @FXML
    private TableColumn<Parkinglot, String> nameCol;
    @FXML
    private TableColumn<Parkinglot, Integer> parksPerRowCol;
    @FXML
    private TableColumn<Parkinglot, Integer> totalCol;


    @FXML
    private void GoBack() throws IOException
    {
        SimpleChatClient.setRoot("MainScreen");
    }

    @FXML
    void initialize()
    {
        EventBus.getDefault().register(this);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        parksPerRowCol.setCellValueFactory(new PropertyValueFactory<>("parksPerRow"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalParkingLots"));
        ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void setParkingTable(ParkingListMessage new_message)
    {
        parkingTable.setItems(FXCollections.observableArrayList(new_message.parkingList));
    }



//    @Subscribe
//    public void setParkingTable(MessageEvent event) {
//        if (inStage) {
//            p_l = (ArrayList<Parkinglot>) event.getMessage().getTable();
//            parkingTable.setItems(FXCollections.observableArrayList(p_l));
//      }
//}


}