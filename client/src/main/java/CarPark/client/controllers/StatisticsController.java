package CarPark.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.LocalDate;

public class StatisticsController {

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

                // Disable all past dates
                setDisable(empty || date.compareTo(today) > 0);
            }
        });
    }
}