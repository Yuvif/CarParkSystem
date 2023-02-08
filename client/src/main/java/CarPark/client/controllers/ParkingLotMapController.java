package CarPark.client.controllers;
import CarPark.client.SimpleClient;
import CarPark.entities.ParkingSlot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingLotMapMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
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


import java.io.IOException;
import java.util.List;
import java.util.Objects;


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

    int rows;
    List<ParkingSlot> parkingSlotsList;

    @FXML
    void getParkingLotRowNum(ActionEvent event)
    {

        ParkingLotMapMessage message = new ParkingLotMapMessage(Message.MessageType.REQUEST, ParkingLotMapMessage.RequestType.GET_ROW,
                parkingLotChoice.getValue());
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParkingLotMapMessage message2 = new ParkingLotMapMessage(Message.MessageType.REQUEST, ParkingLotMapMessage.RequestType.GET_PARKING_SLOTS,
                parkingLotChoice.getValue());
        try {
            SimpleClient.getClient().sendToServer(message2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void chooseFloor(ActionEvent event)
    {
        ParkingLotMapMessage message = new ParkingLotMapMessage(Message.MessageType.REQUEST, ParkingLotMapMessage.RequestType.GET_PARKING_SLOTS, parkingLotChoice.getValue());
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(floorChoice.getValue() == 0)
        {
            createParkingLotMap("A");
            setOccupiedSlots("A");
        }
        else if(floorChoice.getValue() == 1)
        {
            createParkingLotMap("B");
            setOccupiedSlots("B");
        }
        else if(floorChoice.getValue() == 2)
        {
            createParkingLotMap("C");
            setOccupiedSlots("C");
        }
    }

    public boolean isOccupied(String slotId, String parkingLotId)
    {
        for(ParkingSlot parkingSlot : parkingSlotsList)
        {
            if(Objects.equals(Integer.parseInt(parkingLotId), parkingSlot.getParkinglot().getParkingLotId()) &&
                    parkingSlot.getGeneratedValue().contains(slotId) &&
                    Objects.equals(String.valueOf(parkingSlot.getSpotStatus()), "USED"))
            {
                System.out.println(parkingSlot.getParkinglot().getParkingLotId());
                return true;
            }
        }
        return false;
    }

    @FXML
    public void createParkingLotMap(String floor)
    {
        parkingLotMap.getChildren().clear();
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < rows; col++)
            {
                Rectangle rect = new Rectangle(100, 50);
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                //rect.setOnMouseClicked(event -> rect.setFill(Color.GREEN));
                parkingLotMap.add(rect, col, row);
                Text label = new Text(floor + (1 + row * rows + col));

                parkingLotMap.add(label, col, row);
            }
        }
    }

    public String findParkingLotsIndex(String name)
    {
        List<String> parkingLotList = parkingLotChoice.getItems();
        int index = 1;
        for(String pl_name : parkingLotList)
        {
            if(Objects.equals(pl_name, name))
            {
                break;
            }
            index++;
        }
        return String.valueOf(index);
    }

    public void setOccupiedSlots(String floor)
    {
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < rows; col++)
            {
                Text label = new Text(floor + (1 + row * rows + col));
                Rectangle rect = new Rectangle(100, 50);
                if (isOccupied(String.valueOf(label.getText()), findParkingLotsIndex(parkingLotChoice.getValue())))
                {
                    rect.setFill(Color.RED);
                    parkingLotMap.add(rect, col, row);
                    parkingLotMap.add(label, col, row);
                }
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
        floorChoice.setPromptText("Choose Floor");
    }

    @Subscribe
    public void newResponse(ParkingLotMapMessage new_message) {
        switch (new_message.response_type) {
            case SET_ROW:
                Platform.runLater(() -> {
                    this.rows = new_message.rows;
                    createParkingLotMap("A");
                });
                break;
            case SEND_PARKING_SLOTS:
                this.parkingSlotsList = new_message.parkingSlots;
                break;
            case SEND_ARRANGED_MAP:
                break;
            case SEND_PARKING_LOT_MAP:
                break;
        }
    }

}

