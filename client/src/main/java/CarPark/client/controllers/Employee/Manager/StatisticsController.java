package CarPark.client.controllers.Employee.Manager;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Manager;
import CarPark.entities.Statistics;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.StatisticsMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;

import static CarPark.client.controllers.Controller.sendAlert;

public class StatisticsController {

    @FXML
    private Button GetData;


    @FXML
    private ComboBox<String> parkingLotOpt;

    @FXML
    private DatePicker statisticsDate;

    @FXML
    private TableColumn<Statistics, Integer> delayedEntries;

    @FXML
    private TableColumn<Statistics, Integer> totalRevenue;

    @FXML
    private TableColumn<Statistics, Integer> orders;

    @FXML
    private TableColumn<Statistics, Integer> ordersCancelled;

    @FXML
    private TableView<Statistics> table;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        Manager current_manager = (Manager) SimpleClient.getCurrent_user();
        String parkingLotName = current_manager.getParkinglot().getName();
        parkingLotOpt.setPromptText(parkingLotName);
        parkingLotOpt.setDisable(true);
        statisticsDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                // Disable all future dates
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        orders.setCellValueFactory(new PropertyValueFactory<>("numberOfOrders"));
        delayedEntries.setCellValueFactory(new PropertyValueFactory<>("numberOfOrdersLate"));
        ordersCancelled.setCellValueFactory(new PropertyValueFactory<>("numberOfOrdersCancelled"));
        totalRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
    }

    @Subscribe
    public void newResponse(StatisticsMessage msg) throws IOException {
        switch (msg.response_type) {
            case NO_STATISTICS_AVAILABLE -> {
                sendAlert("No data for this date and parking lot", "No info available", Alert.AlertType.WARNING);
            }
            case STATISTICS -> {
                table.setItems(FXCollections.observableArrayList(msg.getStatistics()));
            }
        }
    }
    @FXML
    void GetData(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            Manager current_manager = (Manager) SimpleClient.getCurrent_user();
            String parkingLotId = String.valueOf(current_manager.getParkinglot().getParkingLotId());
            StatisticsMessage msg = new StatisticsMessage(Message.MessageType.REQUEST, StatisticsMessage.RequestType.GET_STATISTICS,
                    parkingLotId, statisticsDate.getValue());
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void GoBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("ParkingLotManagerPage");
    }
}