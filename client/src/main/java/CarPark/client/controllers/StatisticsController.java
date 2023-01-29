package CarPark.client.controllers;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.StatisticsMessage;
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
    private Button GoBack;

    @FXML
    private TableColumn<String, String> noOfOrders;

    @FXML
    private TableColumn<String, String> ordersDelayed;

    @FXML
    private TableColumn<String, String> orderscanceled;

    @FXML
    private ComboBox<String> parkingLotOpt;

    @FXML
    private DatePicker statisticsDate;

    @FXML
    private TableColumn<?, String> totalIncome;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        parkingLotOpt.getItems().add("Haifa (1)");
        parkingLotOpt.getItems().add("Tel Aviv (2)");
        parkingLotOpt.getItems().add("Jerusalem (3)");
        parkingLotOpt.getItems().add("Be'er Sheva (4)");
        parkingLotOpt.getItems().add("Eilat (5)");

        statisticsDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                // Disable all future dates
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        noOfOrders.setCellValueFactory(new PropertyValueFactory<>("numberOfOrders"));
        ordersDelayed.setCellValueFactory(new PropertyValueFactory<>("numberOfOrdersLate"));
        orderscanceled.setCellValueFactory(new PropertyValueFactory<>("numberOfOrdersCancelled"));
        totalIncome.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
//        wait for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        noOfOrders.setText("0");
        ordersDelayed.setText("1");
        orderscanceled.setText("2");
        totalIncome.setText("3");
    }

    @Subscribe
    public void newResponse(StatisticsMessage msg) throws IOException {
        switch (msg.response_type) {
            case NO_STATISTICS_AVAILABLE -> {
                System.out.println("StatisticsController: NO_STATISTICS_AVAILABLE");
                sendAlert("No data for this date and parking lot", "No info available", Alert.AlertType.WARNING);
            }
            case STATISTICS -> {
                System.out.println("StatisticsController: STATISTICS");
                noOfOrders.setText(String.valueOf(msg.getStatistics().getNumberOfOrders()));
                ordersDelayed.setText(String.valueOf(msg.getStatistics().getNumberOfOrdersLate()));
                orderscanceled.setText(String.valueOf(msg.getStatistics().getNumberOfOrdersCancelled()));
                totalIncome.setText(String.valueOf(msg.getStatistics().getTotalRevenue()));
            }
        }
    }
    @FXML
    void GetData(ActionEvent event) throws IOException {
//        initialize a StatisticsMessage with the date and parking lot chosen by the user
//        with regexextract the number of parking lot from the string
        String parkingLot = parkingLotOpt.getValue();
        String parkingLotId = parkingLot.replaceAll("[^0-9]", "");
        StatisticsMessage msg = new StatisticsMessage(Message.MessageType.REQUEST, StatisticsMessage.RequestType.GET_STATISTICS,
                parkingLotId, statisticsDate.getValue());
        SimpleClient.getClient().sendToServer(msg);
        System.out.println("Sent message to server");
    }

    @FXML
    void GoBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("MainScreen");
    }
}