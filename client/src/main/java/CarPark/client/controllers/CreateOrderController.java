//package CarPark.client.controllers;
//
//import javafx.animation.PauseTransition;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.util.Duration;
//import CarPark.entities.Order;
//
//import java.io.IOException;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ResourceBundle;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.time.LocalDate;
//
//
//
//public class CreateOrderController implements Initializable {
//
//    @FXML
//    private ComboBox arrivalHour;
//
//    @FXML
//    private ComboBox arrivalMin;
//
//    @FXML
//    private DatePicker arrivalDate;
//
//    @FXML
//    private TextField carIdTextBox;
//
//    @FXML
//    private TextField emailAddress;
//
//    @FXML
//    private DatePicker estLeavingDate;
//
//    @FXML
//    private ComboBox estLeavingHour;
//
//    @FXML
//    private ComboBox estLeavingMin;
//
//    @FXML
//    private TextField idTextBox;
//
//    @FXML
//    private ComboBox parkingLotsOpt;
//
//    @FXML
//    private Button submitBtn;
//
//    @FXML
//    void chooseArrivalDate(ActionEvent event) {
//
//    }
//
//    @FXML
//    void chooseArrivalHour(ActionEvent event) {
//
//    }
//
//    @FXML
//    void chooseArrivalMin(ActionEvent event)
//    {
//
//    }
//
//    @FXML
//    void chooseEstLeavingHour(ActionEvent event)
//    {
//
//    }
//
//    @FXML
//    void chooseEstLeavingMin(ActionEvent event)
//    {
//
//    }
//
//    @FXML
//    void chooseEstimatedLeavingDate(ActionEvent event) {
//
//    }
//
//    @FXML
//    void chooseParkingLot(ActionEvent event) {
//
//    }
//
//    @FXML
//    void enterCarId(ActionEvent event) {
//    }
//
//    @FXML
//    void enterCustomerId(ActionEvent event) {
//
//    }
//
//    @FXML
//    void enterEmail(ActionEvent event) {
//
//    }
//
//    @FXML
//    void submitDetails(ActionEvent event) {
//        if(!checkValidity())
//        {
//            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
//        }
//        else // create an entity Order and send it to the server
//        {
//            Order order = createOrder();
//            sendOrderToServer(order);
//            Platform.runLater(() -> {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setHeaderText("Order submitted");
//                alert.show();
//                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
//                pause.setOnFinished((e -> {
//                    alert.close();
//                }));
//                pause.play();
//            });
//        }
//    }
//
//    private boolean checkValidity()
//    {
//        if(idTextBox.getText().isEmpty() || carIdTextBox.getText().isEmpty() || emailAddress.getText().isEmpty() || arrivalDate.getValue() == null || estLeavingDate.getValue() == null
//           || arrivalHour.getItems().isEmpty() || arrivalMin.getItems().isEmpty() || estLeavingHour.getItems().isEmpty() || estLeavingMin.getItems().isEmpty() || parkingLotsOpt.getItems().isEmpty())
//        {
//            sendAlert("Some fields have not been filled", " Empty or Missing Fields", Alert.AlertType.WARNING);
//            return false;
//        }
//        else if(!checkEmailValidity())
//        {
//            sendAlert("Email is not valid", " Invalid Email", Alert.AlertType.WARNING);
//            return false;
//        }
//        else if(!checkIdValidity())
//        {
//            sendAlert("ID is not valid", " Invalid ID", Alert.AlertType.WARNING);
//            return false;
//        }
//        else if(!checkCarIdValidity())
//        {
//            sendAlert("Car ID is not valid", " Invalid Car ID", Alert.AlertType.WARNING);
//            return false;
//        }
//        else if(!checkDateValidity())
//        {
//            sendAlert("Date is not valid", " Invalid Date", Alert.AlertType.WARNING);
//            return false;
//        }
//        else
//        {
//            return true;
//        }
//    }
//    private boolean checkEmailValidity()
//    {
//        String email = emailAddress.getText();
//        String regex = "^(.+)@(.+)$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }
//
//    private boolean checkIdValidity()
//    {
//        String id = idTextBox.getText();
//        String regex = "^[0-9]{9}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(id);
//        return matcher.matches();
//    }
//
//    private boolean checkCarIdValidity()
//    {
//        String carId = carIdTextBox.getText();
//        String regex = "^[0-9]{7}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(carId);
//        return matcher.matches();
//    }
//
//    private boolean checkDateValidity()
//    {
//        LocalDate arrival = arrivalDate.getValue();
//        LocalDate leaving = estLeavingDate.getValue();
//        if(arrival.isAfter(leaving))
//        {
//            return false;
//        }
//        else
//        {
//            return true;
//        }
//    }
//
//    private void sendOrderToServer(Order order)
//    {
//        List<Object> msg = new LinkedList<>();
//        msg.add("#CREATE_ORDER");
//        msg.add(order);
//        try {
//            SimpleChatClient.client.sendToServer(msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Order createOrder()
//    {
//        Order order = new Order();
//        order.setCarId(Integer.parseInt(carIdTextBox.getText()));
//        order.setCustomerId(Integer.parseInt(idTextBox.getText()));
//        order.setParkingLotId(Integer.parseInt(parkingLotsOpt.getValue().toString()));
//
//        LocalDate arrival = arrivalDate.getValue();
//        LocalTime arrivalTime = LocalTime.of(Integer.parseInt( arrivalHour.getValue().toString()), Integer.parseInt(arrivalMin.getValue().toString()));
//        LocalDateTime arrivalDateTime = LocalDateTime.of(arrival, arrivalTime);
//        order.setArrivalTime(arrivalDateTime);
//
//        LocalDate leaving = estLeavingDate.getValue();
//        LocalTime leavingTime = LocalTime.of(Integer.parseInt(estLeavingHour.getValue().toString()), Integer.parseInt(estLeavingMin.getValue().toString()));
//        LocalDateTime leavingDateTime = LocalDateTime.of(leaving, leavingTime);
//        order.setEstimatedLeavingTime(leavingDateTime);
//
//        return order;
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle)
//    {
////      think of a way to get the parking lots from the server
//        for(int i = 0; i < 3; i++)
//        {
//            parkingLotsOpt.getItems().add(i);
//        }
//
//        // Initialize the ComboBox with the hours
//        for(int i = 0; i < 24; i++)
//        {
//            if(i < 10)
//            {
//                arrivalHour.getItems().add("0" + i);
//                estLeavingHour.getItems().add("0" + i);
//            }
//            else
//            {
//                arrivalHour.getItems().add(i);
//                estLeavingHour.getItems().add(i);
//            }
//        }
//
//        // Initialize the ComboBox with the minutes
//        for(int i = 0; i < 60; i++)
//        {
//            if(i < 10)
//            {
//                arrivalMin.getItems().add("0" + i);
//                estLeavingMin.getItems().add("0" + i);
//            }
//            else
//            {
//                arrivalMin.getItems().add(i);
//                estLeavingMin.getItems().add(i);
//            }
//        }
//
//        arrivalDate.setDayCellFactory(picker -> new DateCell() {
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                LocalDate today = LocalDate.now();
//
//                // Disable all past dates
//                setDisable(empty || date.compareTo(today) < 0 );
//            }
//        });
//
//        estLeavingDate.setDayCellFactory(picker -> new DateCell() {
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                LocalDate arrivalDateValue = arrivalDate.getValue();
//
//                // Disable all past dates
//                setDisable(empty || date.compareTo(arrivalDateValue) < 0 );
//            }
//        });
//    }
//}
