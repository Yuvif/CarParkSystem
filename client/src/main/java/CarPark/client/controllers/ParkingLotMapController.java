package CarPark.client.controllers;
import CarPark.client.SimpleClient;
import CarPark.entities.ParkingSlot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingLotMapMessage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
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
    private ComboBox<String> floorChoice;

    @FXML
    void goBack(ActionEvent event) {}

    int rows;
    List<ParkingSlot> parkingSlotsList;
    ParkingSlot changedStatus;

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

        if(floorChoice.getValue() == null)
        {
            createParkingLotMap("A");
            setOccupiedSlots("A");
        }
    }


    @FXML
    void chooseFloor(ActionEvent event)
    {
        ParkingLotMapMessage message = new ParkingLotMapMessage(Message.MessageType.REQUEST,
                ParkingLotMapMessage.RequestType.GET_PARKING_SLOTS, parkingLotChoice.getValue());
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Objects.equals(floorChoice.getValue(), "A"))
        {
            createParkingLotMap("A");
            setOccupiedSlots("A");
        }
        else if(Objects.equals(floorChoice.getValue(), "B"))
        {
            createParkingLotMap("B");
            setOccupiedSlots("B");
        }
        else if(Objects.equals(floorChoice.getValue(), "C"))
        {
            createParkingLotMap("C");
            setOccupiedSlots("C");
        }
    }

    public int isOccupied(String slotId, String parkingLotId)
    {
        for(ParkingSlot parkingSlot : parkingSlotsList)
        {
            if(!String.valueOf(parkingSlot.getGeneratedValue().charAt(0)).equals(findParkingLotsIndex(parkingLotChoice.getValue())))
            {
                continue;
            }

            if(Objects.equals(Integer.parseInt(parkingLotId), parkingSlot.getParkinglot().getParkingLotId()) &&
                    parkingSlot.getGeneratedValue().substring(2).equals(slotId) &&
                Objects.equals(String.valueOf(parkingSlot.getSpotStatus()), "EMPTY"))
            {
                return 0;
            }
            else if(Objects.equals(Integer.parseInt(parkingLotId), parkingSlot.getParkinglot().getParkingLotId()) &&
                    parkingSlot.getGeneratedValue().substring(2).equals(slotId) &&
                    Objects.equals(String.valueOf(parkingSlot.getSpotStatus()), "USED"))
            {
                return 1;
            }
            else if(Objects.equals(Integer.parseInt(parkingLotId), parkingSlot.getParkinglot().getParkingLotId()) &&
                    parkingSlot.getGeneratedValue().substring(2).equals(slotId) &&
                    Objects.equals(String.valueOf(parkingSlot.getSpotStatus()), "RESTRICTED"))
            {
                return 2;
            }
            else if(Objects.equals(Integer.parseInt(parkingLotId), parkingSlot.getParkinglot().getParkingLotId()) &&
                    parkingSlot.getGeneratedValue().substring(2).equals(slotId) &&
                    Objects.equals(String.valueOf(parkingSlot.getSpotStatus()), "RESERVED"))
            {
                return 3;
            }
        }
        return -1;
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
                String label = floor + (1 + row * rows + col);

                System.out.println(label);

                Text labelText = new Text(floor + (1 + row * rows + col));
                Rectangle rect = (Rectangle)getNodeByRowColumnIndex(row, col, parkingLotMap);

                if (isOccupied(label, findParkingLotsIndex(parkingLotChoice.getValue())) == 0)
                {
                    rect.setFill(Color.LIGHTGRAY); //dye the rectangle represents the slot in light gray if it's vacant
                    parkingLotMap.add(labelText, col, row);
                }
                else if (isOccupied(label, findParkingLotsIndex(parkingLotChoice.getValue())) == 1)
                {
                    rect.setFill(Color.GREEN); //dye the rectangle represents the parking slot in green if it's occupied
                    parkingLotMap.add(labelText, col, row);
                }
                else if (isOccupied(label, findParkingLotsIndex(parkingLotChoice.getValue())) == 2)
                {
                    rect.setFill(Color.RED); //dye the rectangle represents the parking slot in green if it's faulty
                    parkingLotMap.add(labelText, col, row);
                }
                else if (isOccupied(label, findParkingLotsIndex(parkingLotChoice.getValue())) == 3)
                {
                    rect.setFill(Color.BLUE); //dye the rectangle represents the parking slot in blue if it's reserved
                    parkingLotMap.add(labelText, col, row);
                }

                //By clicking on an empty slot
                Paint currentColor = rect.getFill();
                if(currentColor != Color.GREEN)
                {
                    rect.setOnMouseClicked(event -> {
                        Paint currentColor2 = rect.getFill();
                        if (currentColor2 == Color.BLUE) {
                            rect.setFill(Color.RED);
                        } else if (currentColor2 == Color.RED) {
                            rect.setFill(Color.LIGHTGRAY);
                        } else {
                            rect.setFill(Color.BLUE);
                        }
                    });
                }
            }
        }
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane)
    {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
            {
                result = node;
                break;
            }
        }

        return result;
    }

    @FXML
    void submitChanges(ActionEvent event)
    {
        for (ParkingSlot parkingSlot : parkingSlotsList)
        {
            if(!String.valueOf(parkingSlot.getGeneratedValue().charAt(0)).equals(findParkingLotsIndex(parkingLotChoice.getValue())))
            {
                continue;
            }

            Node rect;
            String label;
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < rows; col++)
                {
                    rect = getNodeByRowColumnIndex(row, col, parkingLotMap);
                    label =  floorChoice.getValue() + (1 + row * rows + col);
                    Rectangle rectangle = (Rectangle)rect;

                    Paint currentColor = rectangle.getFill();

                    System.out.println(currentColor.toString());

                    if (currentColor == Color.RED && parkingSlot.getGeneratedValue().substring(2).equals(label))
                    {
                        System.out.println("1 " + label);
                        parkingSlot.setStatus(ParkingSlot.Status.valueOf("RESTRICTED"));
                    }
                    else if (currentColor == Color.BLUE && parkingSlot.getGeneratedValue().substring(2).equals(label))
                    {
                        System.out.println("2 " + label);
                        parkingSlot.setStatus(ParkingSlot.Status.valueOf("RESERVED"));
                    }
                    else if (currentColor == Color.LIGHTGRAY && parkingSlot.getGeneratedValue().substring(2).equals(label))
                    {
                        System.out.println("3 " + label);
                        parkingSlot.setStatus(ParkingSlot.Status.valueOf("EMPTY"));
                    }
                }
            }
        }

        ParkingLotMapMessage message = new ParkingLotMapMessage(Message.MessageType.REQUEST,
                ParkingLotMapMessage.RequestType.SHOW_PARKING_LOT_MAP, parkingSlotsList);
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
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

        floorChoice.getItems().add("A");
        floorChoice.getItems().add("B");
        floorChoice.getItems().add("C");
        floorChoice.setPromptText("Choose Floor");
    }

    @Subscribe
    public void newResponse(ParkingLotMapMessage new_message) {
        switch (new_message.response_type) {
            case SET_ROW:
                Platform.runLater(() -> {
                    this.rows = new_message.rows;
                    floorChoice.setValue("Floor");
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

