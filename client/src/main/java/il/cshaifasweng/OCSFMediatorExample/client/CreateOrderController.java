package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;

import static il.cshaifasweng.OCSFMediatorExample.client.Controller.sendAlert;

public class CreateOrderController {

    @FXML
    private ComboBox arrivalHour;

    @FXML
    private ComboBox arrivalMin;

    @FXML
    private DatePicker arrivalDate;

    @FXML
    private TextField carIdTextBox;

    @FXML
    private TextField emailAddress;

    @FXML
    private DatePicker estLeavingDate;

    @FXML
    private ComboBox estLeavingHour;

    @FXML
    private ComboBox estLeavingMin;

    @FXML
    private TextField idTextBox;

    @FXML
    private MenuButton parkingLotsOpt;

    @FXML
    private Button submitBtn;

    @FXML
    void chooseArrivalDate(ActionEvent event) {
        arrivalDate.setValue(LocalDate.now());
    }

    @FXML
    void chooseArrivalHour(ActionEvent event) {
        // Initialize the menu button with the hours
        MenuItem[] hours = new MenuItem[24];
        for(int i = 0; i < 24; i++)
        {
            hours[i] = new MenuItem(Integer.toString(i));
            arrivalHour.getItems().add(hours[i]);
        }
    }

    @FXML
    void chooseArrivalMin(ActionEvent event)
    {
        MenuItem[] menuItems = new MenuItem[60];
        for(int i = 0; i < 60; i++)
        {
            menuItems[i] = new MenuItem(Integer.toString(i));
            arrivalMin.getItems().add(menuItems[i]);
        }
    }

    @FXML
    void chooseEstLeavingHour(ActionEvent event)
    {
        for(int i = 0; i < 24; i++)
        {
            arrivalHour.getItems().add(i);
        }
    }

    @FXML
    void chooseEstLeavingMin(ActionEvent event)
    {
//        MenuItem[] menuItems = new MenuItem[60];
//        for(int i = 0; i < 60; i++)
//        {
//            menuItems[i] = new MenuItem(Integer.toString(i));
//            arrivalMin.getItems().add(menuItems[i]);
//        }
    }

    @FXML
    void chooseEstimatedLeavingDate(ActionEvent event) {
        estLeavingDate.setValue(LocalDate.now());
    }

    @FXML
    void chooseParkingLot(ActionEvent event) {
        MenuItem[] parkingLots = new MenuItem[3];
//      think of a way to get the parking lots from the server
        for(int i = 0; i < 3; i++)
        {
            parkingLots[i] = new MenuItem(Integer.toString(i));
            parkingLotsOpt.getItems().add(parkingLots[i]);
        }
    }

    @FXML
    void enterCarId(ActionEvent event) {
    }

    @FXML
    void enterCustomerId(ActionEvent event) {

    }

    @FXML
    void enterEmail(ActionEvent event) {

    }

    @FXML
    void submitDetails(ActionEvent event) {
        if(checkValidity())
        {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);

        }
        else
        {
            // create an entity Order and send it to the server
            Order order = new Order();
            order.setCarId(Integer.parseInt(carIdTextBox.getText()));
            order.setCustomerId(Integer.parseInt(idTextBox.getText()));
            order.setParkingLotId(Integer.parseInt(parkingLotsOpt.getText()));
            LocalDate localDate = arrivalDate.getValue();
            order.setArrivalTime(LocalDateTime.from(localDate));
            order.setEstimatedLeavingTime(LocalDateTime.from(estLeavingDate.getValue()));
            List<Object> msg = new LinkedList<>();
            msg.add("#CREATE_ORDER");
            msg.add(order);

            try {
                SimpleChatClient.client.sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Order submitted");
                alert.show();
                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                pause.setOnFinished((e -> {
                    alert.close();
                }));
                pause.play();
            });

        }
    }

    private boolean checkValidity()
    {
        if(idTextBox.getText().isEmpty() || carIdTextBox.getText().isEmpty() || emailAddress.getText().isEmpty() || arrivalDate.getValue() == null || estLeavingDate.getValue() == null)
        {
            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
            return false;
        }
        else if(!checkEmailValidity())
        {
            sendAlert("Email is not valid", " Invalid Email", Alert.AlertType.WARNING);
            return false;
        }
        else if(!checkIdValidity())
        {
            sendAlert("ID is not valid", " Invalid ID", Alert.AlertType.WARNING);
            return false;
        }
        else if(!checkCarIdValidity())
        {
            sendAlert("Car ID is not valid", " Invalid Car ID", Alert.AlertType.WARNING);
            return false;
        }
        else if(!checkDateValidity())
        {
            sendAlert("Date is not valid", " Invalid Date", Alert.AlertType.WARNING);
            return false;
        }
        else
        {
            return true;
        }
    }
    private boolean checkEmailValidity()
    {
        String email = emailAddress.getText();
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean checkIdValidity()
    {
        String id = idTextBox.getText();
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    private boolean checkCarIdValidity()
    {
        String carId = carIdTextBox.getText();
        String regex = "^[0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(carId);
        return matcher.matches();
    }

    private boolean checkDateValidity()
    {
        LocalDate arrival = arrivalDate.getValue();
        LocalDate leaving = estLeavingDate.getValue();
        if(arrival.isAfter(leaving))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void sendOrderToServer(Order order)
    {

    }

}
