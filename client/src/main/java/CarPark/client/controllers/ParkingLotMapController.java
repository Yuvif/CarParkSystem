package CarPark.client.controllers;
import CarPark.entities.messages.ParkingLotMapMessage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;


public class ParkingLotMapController {

    @FXML
    private AnchorPane window;

    @FXML
    private GridPane parkingLotMap;

    @FXML
    private Button backBtn;

    @FXML
    private ComboBox<String> parkingLotChoice;

    @FXML
    private ComboBox<Integer> floorChoice;

    @FXML
    void goBack(ActionEvent event) {}

    @FXML
    public void createParkingLotMap()
    {
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 7; col++)
            {
                Rectangle rect = new Rectangle(100, 50);
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                rect.setOnMouseClicked(event -> rect.setFill(Color.GREEN));
                parkingLotMap.add(rect, col, row);
                Text label = new Text(Integer.toString(row * 7 + col));
                parkingLotMap.add(label, col, row);
            }
        }
    }

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        parkingLotChoice.getItems().add("Haifa");
        parkingLotChoice.getItems().add("Tel Aviv");
        parkingLotChoice.getItems().add("Jerusalem");
        parkingLotChoice.getItems().add("Be'er Sheva");
        parkingLotChoice.getItems().add("Eilat");

        floorChoice.getItems().add(0);
        floorChoice.getItems().add(1);
        floorChoice.getItems().add(2);
        createParkingLotMap();

    }

    @Subscribe
    public void newResponse(ParkingLotMapMessage new_message) {
        switch (new_message.response_type) {
            case SEND_ARRANGED_MAP:
                break;
            case SEND_PARKING_LOT_MAP:
                break;
        }
    }


}

