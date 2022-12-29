package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class ParkingListController {
    @FXML
    private TableView<Parkinglot> parkingTable;
    @FXML
    private TableColumn<Parkinglot, String> nameCol;
    @FXML
    private TableColumn<Parkinglot, Integer> parksPerRowCol;
    @FXML
    private TableColumn<Parkinglot, Integer>  totalCol;

    private static ArrayList<Parkinglot> p_l;
    private static boolean inStage;
    private static int msgId = 0;

    @FXML
    private void GoBack() throws IOException {
        inStage = false;
        SimpleChatClient.setRoot("MainScreen");
    }

    @FXML
    void initialize() {
        inStage = true;
        EventBus.getDefault().register(this);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parksPerRowCol.setCellValueFactory(new PropertyValueFactory<>("parksPerRow"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalParkingLots"));
        if (msgId > 0){
            parkingTable.setItems(FXCollections.observableArrayList(p_l));
        }
        else {
            Message msg = new Message(msgId++, "get parking list");

            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Subscribe
    public void setParkingTable(MessageEvent event) {
        if (inStage) {
            p_l = (ArrayList<Parkinglot>) event.getMessage().getTable();
            parkingTable.setItems(FXCollections.observableArrayList(p_l));
        }
    }



}
