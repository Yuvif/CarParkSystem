package CarPark.client.controllers;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.StatisticsMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;

import static CarPark.client.controllers.Controller.sendAlert;

public class StatisticsController {

    @FXML
    private Button GetData;

    @FXML
    private Button GoBack;

    @FXML
    private TableView<Integer> StatisticsTable;

    @FXML
    private ComboBox<String> parkingLotOpt;

    @FXML
    private DatePicker statisticsDate;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        parkingLotOpt.getItems().add("Haifa");
        parkingLotOpt.getItems().add("Tel Aviv");
        parkingLotOpt.getItems().add("Jerusalem");
        parkingLotOpt.getItems().add("Be'er Sheva");
        parkingLotOpt.getItems().add("Eilat");

        statisticsDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                // Disable all future dates
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
    }

    @Subscribe
    public void newResponce(StatisticsMessage msg) throws IOException {
        switch (msg.response_type) {
            case NO_STATISTICS_AVAILABLE -> {
                sendAlert("No data for this date and parking lot", "No info available", Alert.AlertType.WARNING);
            }
            case STATISTICS -> {
                 StatisticsTable.getItems().add(msg.getStatistics().getNumberOfOrders());
                 StatisticsTable.getItems().add(msg.getStatistics().getNumberOfOrdersCancelled());
                 StatisticsTable.getItems().add(msg.getStatistics().getNumberOfOrdersLate());
                 StatisticsTable.getItems().add(msg.getStatistics().getTotalRevenue());
            }
        }
    }
    @FXML
    void GetData(ActionEvent event) throws IOException {
//        initialize a StatisticsMessage with the date and parking lot chosen by the user
        StatisticsMessage msg = new StatisticsMessage(Message.MessageType.REQUEST, StatisticsMessage.RequestType.GET_STATISTICS,
                parkingLotOpt.getValue(), statisticsDate.getValue());
        SimpleClient.getClient().sendToServer(msg);
    }

    @FXML
    void GoBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("MainScreen");
    }
}