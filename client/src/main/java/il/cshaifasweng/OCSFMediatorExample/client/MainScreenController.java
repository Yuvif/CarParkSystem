package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MainScreenController{
    private int msgId;

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
        try {
            Message message = new Message(msgId, "main screen request");
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void Parking() throws IOException {
        SimpleChatClient.setRoot("ParkingList");
    }

    @FXML
    private void PricesTable() throws IOException{
        SimpleChatClient.setRoot("PricesTable");
    }


}
