package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class ParkingListController  extends Controller{
    @FXML
    private TableView<Parkinglot> parkingTable;
    @FXML
    private TableColumn<Parkinglot, String> nameCol;
    @FXML
    private TableColumn<Parkinglot, Integer> parksPerRowCol;
    @FXML
    private TableColumn<Parkinglot, Integer>  totalCol;
//
//    private static ArrayList<Parkinglot> p_l;
//    private static boolean inStage;
//    private static int msgId = 0;

    @FXML
    private void GoBack() throws IOException {
//        inStage = false;
        SimpleChatClient.setRoot("MainScreen");
    }

    @FXML
    void initialize() {

        nameCol.setCellValueFactory(new PropertyValueFactory<Parkinglot,String>("id"));
        parksPerRowCol.setCellValueFactory(new PropertyValueFactory<Parkinglot,Integer>("parksPerRow"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Parkinglot,Integer>("totalParkingLots"));


        SimpleChatClient.client.setController(this);
        List<Object> msg = new LinkedList<>();
        msg.add("#PULL_PARKINGLOTS");

        try {
            SimpleChatClient.client.sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void pullParkinglots(ObservableList<Parkinglot> parkinglots) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                parkingTable.setItems(parkinglots);

            }
        });
    }

//    @Subscribe
//    public void setParkingTable(MessageEvent event) {
//        if (inStage) {
//            p_l = (ArrayList<Parkinglot>) event.getMessage().getTable();
//            parkingTable.setItems(FXCollections.observableArrayList(p_l));
//      }
//}



}