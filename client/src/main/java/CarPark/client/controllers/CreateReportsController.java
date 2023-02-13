package CarPark.client.controllers;
import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.*;
import CarPark.entities.messages.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;

public class CreateReportsController extends AbstractReports {

    @FXML
    private StackedBarChart<String, Number> complaintChart;

    @FXML
    private StackedBarChart<String, Number> ordrsChart1;

    @FXML
    private StackedBarChart<String, Number> restrictedPSlotsChart;

    @FXML
    private CategoryAxis daysAxis3;

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
    private ComboBox<String> plPicker;

    @FXML
    private Label membershipsLabel;

    @FXML
    private Label ordersLabel;

    @FXML
    private Label customersLabel;



    @FXML
    void initialize() throws IOException {

        assert plPicker != null : "fx:id=\"plPicker\" was not injected: check your FXML file 'Login.fxml'.";

        EventBus.getDefault().register(this);
        ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
        SimpleClient.getClient().sendToServer(msg);

        Manager current_manager = (Manager) SimpleClient.getCurrent_user();
        String parkingLotName = current_manager.getParkinglot().getName();
        plPicker.setPromptText(parkingLotName);
        plPicker.setDisable(true);
    }
    /**
     * makeReport function activates if pressing the make report button, first it makes
     * sure that a valid time interval and store has been inserted, and then sends to the server
     * a request for all orders and complaints that are relevant for the request.
     */

    @FXML
    void goBack(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("ParkingLotManagerPage");
    }

    @FXML
    void makeReport(ActionEvent event) throws InterruptedException, IOException {
        //EventBus.getDefault().register(this);
        coolButtonClick((Button) event.getTarget());
        if (isInvalid())
            sendAlert("Must pick time interval to make a report!", "Date Missing", Alert.AlertType.ERROR);

        else {   // send request to server to pull data for report, with store and date interval
            //--will be an auto choosing of the specific parkinglot of the P-l manager (after login):
            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
            //temporary - simply choose a random parkinglot:
            //msg.add(((User) App.client.user).getStore());


            String parkingLotName;
//            if (SimpleClient.getCurrent_user() instanceof ParkingLotWorker)
//            {
//                //parking lot worker of specific parking lot
//                ParkingLotWorker parkingLotWorker = (ParkingLotWorker)SimpleClient.getCurrent_user();
//                parkingLotName = parkingLotWorker.getParkinglot().getName();
//            }
//            else //customer service worker - general worker
//            {

           // }

            Manager current_manager = (Manager) SimpleClient.getCurrent_user();
            parkingLotName = current_manager.getParkinglot().getName();
            plPicker.setPromptText(parkingLotName);
            plPicker.setDisable(true);

            Date from = getPickedDate(fromDate);
            Date to = addDays(getPickedDate(toDate), 1);
            OrderMessage ordersMsg = new OrderMessage(Message.MessageType.REQUEST,
                    OrderMessage.RequestType.GET_SELECTED_ORDERS, parkingLotName,from, to );
            SimpleClient.getClient().sendToServer(ordersMsg);

            PullParkingSlotsMessage pslotMsg = new PullParkingSlotsMessage(Message.MessageType.REQUEST,
                    PullParkingSlotsMessage.RequestType.GET_PARKING_SLOTS_REP, parkingLotName, from, to );
            SimpleClient.getClient().sendToServer(pslotMsg);

            ComplaintMessage comMsg = new ComplaintMessage(Message.MessageType.REQUEST,
                    ComplaintMessage.RequestType.GET_COMPLAINTS_REP, parkingLotName );
            SimpleClient.getClient().sendToServer(comMsg);
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
    public void newResponse(OrderMessage new_message) {
        System.out.println("we got controller back from order reports message");

        switch (new_message.response_type) {
            case SET_SELECTED_ORDERS -> Platform.runLater(() -> showOrders((LinkedList<Order>) new_message.ordersList));
        }
    }
    @Subscribe
    public void newResponse(PullParkingSlotsMessage new_message) {
        System.out.println("we got controller back from pslots message");
        switch (new_message.response_type) {
            case SET_PARKING_SLOTS_REP -> Platform.runLater(()-> showRestrictedPSlots(new_message.parkingSlots));
        }
    }
    @Subscribe
    public void newResponse(ComplaintMessage new_message) {
        System.out.println("we got controller back from complaint message");
        switch (new_message.response_type) {
            case SET_ALL_COMPLAINTS -> Platform.runLater(()->{
                //LinkedList<Complaint> res = new LinkedList<>(new_message.complaints2Rep);
                showComplaints(new_message.complaints2Rep);
            });
        }
    }

    @Subscribe
    public void setParkingTable(ParkingListMessage new_message) {
        for (Parkinglot s : new_message.parkingList)
            plPicker.getItems().add(s.getId());
    }

    /**
     * showOrders function gets all relevant orders, then it gets a map from getMap that maps from product name
     * to the amount the store sold, and from that data, displaying it with the PieChart.
     * also add a "handle" function to the chart that displays the amount that the product sold when clicking
     * on the PieChart.
     */

    //need to sort the orders arrangement by customer type!!!!----------------------------------------------
    //need to add a date attribute to the order entity declaration!!!---------------------------------------
//    private void showOrders(LinkedList<Order> orders) {
//
//        ordrsChart1.getData().clear();
//        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
//        //series.setName(Complaint.topicToString(topic));
//        seriesLinkedList.add(series);
//
//
//        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
//
//
//
//                Instant instant = order.getArrivalTime().atZone(ZoneId.systemDefault()).toInstant();
//                Date date1 = Date.from(instant);
//                if (dateAreEqual(dateToLocalDate(date1), date))
//                {
//                    switch (order.getCustomer().getMemberships().size()){
//                        case 0:
//                            break;
//                        default: {
//                            numofMem += 1;
//                            break;
//                        }
//                    }
//                    numOfOrd += 1;
//                }
//            }
//
//            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfOrd - numofMem));
//            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numofMem));
//        }
//        ordrsChart1.getData().addAll(seriesLinkedList);
//    }
//
//}

    private void showOrders(LinkedList<Order> orders) {
//        complaintChart.getData().clear();
//        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//        XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
//        series1.setName("Memberships");
//        seriesLinkedList.add(series1);
//
//        XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
//        series2.setName("Customers");
//        seriesLinkedList.add(series2);

        int days = getDatesBetween(fromDate.getValue(), toDate.getValue()).size();
        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
            int numOfCustomers = 0;
            int numOfMem = 0;

            for (Order order : orders) {
                if (dateAreEqual(dateToLocalDate(order.getDate()), date)) {
                    if (order.getCustomer().getMemberships().size() > 0) {
                        numOfMem += 1;
                    } else {
                        numOfCustomers += 1;
                    }
                }
            }

            ordersLabel.setText(String.valueOf(numOfCustomers + numOfMem));
            customersLabel.setText(String.valueOf(numOfCustomers));
            membershipsLabel.setText(String.valueOf(numOfMem));

//            for (int j = 0; j < days; j++) {
//                series1.getData().add(new XYChart.Data<>(String.valueOf(j), numOfMem[j]));
//                series2.getData().add(new XYChart.Data<>(String.valueOf(j), numOfOrd[j]));
//            }
//
//           //series1.getData().add(new XYChart.Data<String, Integer[]>(formatter.format(localDateToDate(date)), numOfOrd));
//            //series2.getData().add(new XYChart.Data<String, Integer[]>(formatter.format(localDateToDate(date)), numOfMem));
//        }
//        ordrsChart1.getData().addAll(seriesLinkedList);
//
//        LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());
//        chart.getData().add(series1);
//        chart.getData().add(series2);
        }
    }



    private void showRestrictedPSlots(LinkedList<ParkingSlot> parkingSlots) {
        restrictedPSlotsChart.getData().clear();
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
        }

        restrictedPSlotsChart.getData().addAll(seriesLinkedList);
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
