package CarPark.client.controllers;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Complaint;
import CarPark.entities.Order;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.ParkingSlotsMessage;
import CarPark.entities.messages.PullOrdersMessage;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.PullParkingSlotsMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

public class CreateReportsController extends AbstractReports {

    @FXML
    private StackedBarChart<String, Number> complaintChart;

    @FXML
    private StackedBarChart<String, Number> restrictedPslotsChart;

    @FXML
    private StackedBarChart<String, Number> ordersChart;
    @FXML
    private CategoryAxis daysAxis3;

    @FXML
    private StackedBarChart<?, ?> ordrsChart1;

    @FXML
    private NumberAxis ordsAxis1;

    @FXML // fx:id="daysAxis1"
    private CategoryAxis daysAxis1; // Value injected by FXMLLoader

    @FXML // fx:id="makeReportBtn"
    private Button makeReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="restricPslotAxis1"
    private NumberAxis restricPslotAxis1; // Value injected by FXMLLoader

    @FXML
    private NumberAxis complaintsAxis;

    @FXML
    private CategoryAxis daysAxis;

    @FXML
    private DatePicker fromDate;

    @FXML
    private Label salesNum;

    @FXML
    private DatePicker toDate;

    @FXML
    void initialize() {
        displayDates(fromDate, LocalDate.now(), true);
        //temporary - simply choose a random parkinglot:
    }
    /**
     * makeReport function activates if pressing the make report button, first it makes
     * sure that a valid time interval and store has been inserted, and then sends to the server
     * a request for all orders and complaints that are relevant for the request.
     */

    @FXML
    void makeReport(ActionEvent event) throws InterruptedException, IOException {
        EventBus.getDefault().register(this);
        coolButtonClick((Button) event.getTarget());
        salesNum.setText("");
        if (isInvalid())
            sendAlert("Must pick time interval to make a report!", "Date Missing", Alert.AlertType.ERROR);

        else {   // send request to server to pull data for report, with store and date interval
            //--will be an auto choosing of the specific parkinglot of the P-l manager (after login):
            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
            //temporary - simply choose a random parkinglot:
            //msg.add(((User) App.client.user).getStore());
            String parkinglot = "CPS Eilat";
            Date from = getPickedDate(fromDate);
            Date to = addDays(getPickedDate(toDate), 1);
            PullOrdersMessage ordersMsg = new PullOrdersMessage(Message.MessageType.REQUEST, PullOrdersMessage.RequestType.GET_SELECTED_ORDERS, parkinglot,from, to );
            SimpleClient.getClient().sendToServer(ordersMsg);
            PullParkingSlotsMessage pslotMsg = new PullParkingSlotsMessage(Message.MessageType.REQUEST, PullParkingSlotsMessage.RequestType.GET_PARKING_SLOTS_REP, parkinglot,from, to );
            SimpleClient.getClient().sendToServer(pslotMsg);
//            ParkingSlotsMessage pslotMsg = new ParkingSlotsMessage(Message.MessageType.REQUEST, ParkingSlotsMessage.RequestType.GET_SELECTED_PARKING_SLOTS, parkinglot,from, to );
//            SimpleClient.getClient().sendToServer(pslotMsg);
//            SimpleChatClient.client.setController(this);-------------------------------temporary fix;
        }
    }

    /**
     * changedFromDate and changedToDate functions activates when the user changes the DatePicker value,
     * then they display the right data that the user could choose from on the other DatePicker.
     */

    public void changedFromDate(ActionEvent event) throws InterruptedException {
        toDate.setDisable(false);
        if (numOfDays(fromDate.getValue(), LocalDate.now()) <= 31)
            displayDates(toDate, fromDate.getValue(), LocalDate.now());

        else
            displayDates(toDate, fromDate.getValue(), addLocalDate(fromDate, 30));
    }

    public void changedToDate(ActionEvent event) throws InterruptedException {
        toDate.setDisable(false);
        displayDates(fromDate, addLocalDate(toDate, -30), toDate.getValue());
    }

    @Subscribe
    public void newResponse(PullOrdersMessage new_message) {
        System.out.println("we got controller back from reports message");
        switch (new_message.response_type) {
            case SET_SELECTED_ORDERS:
                showOrders(new_message.orders);
                break;
//            case SET_SELECTED_COMPLAINTS:
//                showComplaints(new_message.complaints);
//                break;
        }
    }
    @Subscribe
    public void newResponse(PullParkingSlotsMessage new_message) {
        System.out.println("we got controller back from reports message");
        switch (new_message.response_type) {
            case SET_PARKING_SLOTS_REP:
                showRestrictedPSlots(new_message.parkingSlots);
                break;
        }
    }
    /**
     * showOrders function gets all relevant orders, then it gets a map from getMap that maps from product name
     * to the amount the store sold, and from that data, displaying it with the PieChart.
     * also add a "handle" function to the chart that displays the amount that the product sold when clicking
     * on the PieChart.
     */

    //need to sort the orders arrangement by customer type!!!!----------------------------------------------
    //need to add a date attribute to the order entity declaration!!!---------------------------------------
    private void showOrders(LinkedList<Order> orders) {
        ordersChart.getData().clear();
        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        //series.setName(Complaint.topicToString(topic));
        seriesLinkedList.add(series);

        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
            int numOfOrd = 0;
            for (Order order : orders) {
                if (dateAreEqual(dateToLocalDate(order.getDate()), date))
                    numOfOrd += 1;
            }

            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfOrd));
        }
        complaintChart.getData().addAll(seriesLinkedList);
    }

    private void showRestrictedPSlots(LinkedList<ParkingSlot> parkingSlots) {
        restrictedPslotsChart.getData().clear();
        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        //series.setName(Complaint.topicToString(topic));
        seriesLinkedList.add(series);

        //need to implement a date attribute for EditParkingSlots feature (RESTRICTED)!!!
        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
            int numOfRestPSl = 0;
            for (ParkingSlot parkingSlot : parkingSlots) {
                if (parkingSlot.getStatus().equals(ParkingSlot.Status.RESTRICTED))
                    numOfRestPSl += 1;
            }

            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfRestPSl));
            salesNum.setText(String.valueOf(numOfRestPSl));
        }

        restrictedPslotsChart.getData().addAll(seriesLinkedList);
    }

    private void showComplaints(LinkedList<Complaint> complaints) {
        complaintChart.getData().clear();
        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        //series.setName(Complaint.topicToString(topic));
        seriesLinkedList.add(series);

        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
            int numOfComp = 0;
            for (Complaint complaint : complaints) {
                if (dateAreEqual(dateToLocalDate(complaint.getDate()), date))
                    numOfComp += 1;
            }

            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfComp));
        }
        complaintChart.getData().addAll(seriesLinkedList);
    }

    /**
     * @return if the time interval data filled validly
     */

    public boolean isInvalid() {
        return toDate.isDisabled() || toDate.getValue() == null;
    }
}
