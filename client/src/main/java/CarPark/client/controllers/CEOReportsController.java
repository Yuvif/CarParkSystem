//package CarPark.client.controllers;
//import CarPark.client.SimpleClient;
//import CarPark.entities.Complaint;
//import CarPark.entities.Order;
//import CarPark.entities.ParkingSlot;
//import CarPark.entities.Parkinglot;
//import CarPark.entities.messages.*;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.StackedBarChart;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.*;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.LinkedList;
//
//public class CEOReportsController extends AbstractReports {
//    @FXML
//    private StackedBarChart<String,Number> complaintChart;
//
//    @FXML
//    private NumberAxis complaintsAxis;
//
//    @FXML
//    private CategoryAxis daysAxis;
//
//    @FXML
//    private CategoryAxis daysAxis1;
//
//    @FXML
//    private CategoryAxis daysAxis3;
//
//    @FXML
//    private DatePicker fromDate;
//
//    @FXML
//    private Button makeCompBtn;
//
//    @FXML
//    private Button makeOrdersBtn1;
//
//    @FXML
//    private Button makePslotsButton;
//
//    @FXML
//    private StackedBarChart<String,Number> ordersChart1;
//
//    @FXML
//    private NumberAxis ordsAxis1;
//
//    @FXML
//    private ComboBox<String> plPicker;
//
//    @FXML
//    private NumberAxis restricPslotAxis1;
//
//    @FXML
//    private StackedBarChart<String,Number> restrictedPSlotsChart;
//
//    @FXML
//    private Label salesNum;
//
//    @FXML
//    private DatePicker toDate;
//
//    @FXML
//    void initialize() throws IOException {
//        displayDates(fromDate, LocalDate.now(), true);
//        //temporary - simply choose a random parkinglot:
//        assert plPicker != null : "fx:id=\"plPicker\" was not injected: check your FXML file 'Login.fxml'.";
//        displayDates(fromDate, LocalDate.now(), true);
//
//        EventBus.getDefault().register(this);
//        ParkingListMessage msg = new ParkingListMessage(Message.MessageType.REQUEST, ParkingListMessage.RequestType.GET_ALL_PARKING_LOTS);
//        SimpleClient.getClient().sendToServer(msg);
//        plPicker.getItems().add("Set Parkinglot");
//        //plPicker.setValue("Set Parkinglot");
//    }
//    /**
//     * makeReport function activates if pressing the make report button, first it makes
//     * sure that a valid time interval and PLOT has been inserted, and then sends to the server
//     * a request for all orders and complaints that are relevant for the request.
//     */
//
//    @FXML
//    void makeOrdersReport(ActionEvent event) throws IOException, InterruptedException {
//        //EventBus.getDefault().register(this);
//        coolButtonClick((Button) event.getTarget());
//        salesNum.setText("");
//        if (isInvalid())
//            sendAlert("Must pick time interval to make a report!", "Date Missing", Alert.AlertType.ERROR);
//
//        else {   // send request to server to pull data for report, with store and date interval
//            //--will be an auto choosing of the specific parkinglot of the P-l manager (after login):
//            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
//            //temporary - simply choose a random parkinglot:
//            String parkinglot = "CPS Eilat";
//            Date from = getPickedDate(fromDate);
//            Date to = addDays(getPickedDate(toDate), 1);
//            OrderMessage ordersMsg = new OrderMessage(Message.MessageType.REQUEST, OrderMessage.RequestType.GET_SELECTED_ORDERS, parkinglot,from, to );
//            SimpleClient.getClient().sendToServer(ordersMsg);
//        }
//    }
//
//    @FXML
//    void makePslotsReport(ActionEvent event) throws IOException, InterruptedException {
//        //EventBus.getDefault().register(this);
//        coolButtonClick((Button) event.getTarget());
//        salesNum.setText("");
//        if (isInvalid())
//            sendAlert("Must pick time interval to make a report!", "Date Missing", Alert.AlertType.ERROR);
//
//        else {   // send request to server to pull data for report, with store and date interval
//            //--will be an auto choosing of the specific parkinglot of the P-l manager (after login):
//            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
//            //temporary - simply choose a random parkinglot:
//            String parkinglot = "CPS Eilat";
//            Date from = getPickedDate(fromDate);
//            Date to = addDays(getPickedDate(toDate), 1);
//
//            PullParkingSlotsMessage pslotMsg = new PullParkingSlotsMessage(Message.MessageType.REQUEST, PullParkingSlotsMessage.RequestType.GET_PARKING_SLOTS_REP, parkinglot,from, to );
//            SimpleClient.getClient().sendToServer(pslotMsg);
//        }
//    }
//    @FXML
//    void makeCompReport(ActionEvent event) throws InterruptedException, IOException {
//        //EventBus.getDefault().register(this);
//        coolButtonClick((Button) event.getTarget());
//        salesNum.setText("");
//        if (isInvalid())
//            sendAlert("Must pick time interval to make a report!", "Date Missing", Alert.AlertType.ERROR);
//
//        else {   // send request to server to pull data for report, with store and date interval
//            //--will be an auto choosing of the specific parkinglot of the P-l manager (after login):
//            //String parkinglot = SimpleChatClient.client.getParkinglot().getName();
//            //temporary - simply choose a random parkinglot:
//            String parkinglot = "CPS Eilat";
//            Date from = getPickedDate(fromDate);
//            Date to = addDays(getPickedDate(toDate), 1);
//            ComplaintMessage comMsg = new ComplaintMessage(Message.MessageType.REQUEST, ComplaintMessage.RequestType.GET_ALL_COMPLAINTS, parkinglot );
//            SimpleClient.getClient().sendToServer(comMsg);
//        }
//    }
//
//    /**
//     * changedFromDate and changedToDate functions activates when the user changes the DatePicker value,
//     * then they display the right data that the user could choose from on the other DatePicker.
//     */
//
//    public void changedFromDate(ActionEvent event) throws InterruptedException {
//        toDate.setDisable(false);
//        if (numOfDays(fromDate.getValue(), LocalDate.now()) <= 31)
//            displayDates(toDate, fromDate.getValue(), LocalDate.now());
//
//        else
//            displayDates(toDate, fromDate.getValue(), addLocalDate(fromDate, 30));
//    }
//
//    public void changedToDate(ActionEvent event) throws InterruptedException {
//        toDate.setDisable(false);
//        displayDates(fromDate, addLocalDate(toDate, -30), toDate.getValue());
//    }
//
//    @Subscribe
//    public void setParkingTable(ParkingListMessage new_message) {
//        for (Parkinglot s : new_message.parkingList)
//            plPicker.getItems().add(s.getId());
//    }
//
//    @Subscribe
//    public void newResponse(OrderMessage new_message) {
//        System.out.println("we got controller back from order reports message");
//
//        switch (new_message.response_type) {
//            case SET_SELECTED_ORDERS -> Platform.runLater(() -> {
//                showOrders((LinkedList<Order>) new_message.ordersList);
//            });
//        }
//    }
//    @Subscribe
//    public void newResponse(PullParkingSlotsMessage new_message) {
//        System.out.println("we got controller back from pslots message");
//        switch (new_message.response_type) {
//            case SET_PARKING_SLOTS_REP -> Platform.runLater(()-> {
//                showRestrictedPSlots(new_message.parkingSlots);
//
//            });
//        }
//    }
//    @Subscribe
//    public void newResponse(ComplaintMessage new_message) {
//        System.out.println("we got controller back from complaints message");
//        switch (new_message.response_type) {
//            case SET_ALL_COMPLAINTS -> Platform.runLater(()-> {
//                //case ALL_COMPLAINTS:
//                    showComplaints(new_message.complaints2Rep);
//            });
//        }
//    }
//    /**
//     * showOrders function gets all relevant orders, then it gets a map from getMap that maps from product name
//     * to the amount the store sold, and from that data, displaying it with the PieChart.
//     * also add a "handle" function to the chart that displays the amount that the product sold when clicking
//     * on the PieChart.
//     * @param // orders from server
//     */
//
//    //need to sort the orders arrangement by customer type!!!!----------------------------------------------
//    //need to add a date attribute to the order entity declaration!!!---------------------------------------
//    private void showOrders(LinkedList<Order> orders) {
//
//        ordersChart1.getData().clear();
//        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
//        //series.setName(Complaint.topicToString(topic));
//        seriesLinkedList.add(series);
//
//        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
//            int numOfOrd = 0;
//            for (Order order : orders) {
//                if (dateAreEqual(dateToLocalDate(order.getDate()), date))
//                    numOfOrd += 1;
//            }
//
//            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfOrd));
//        }
//        ordersChart1.getData().addAll(seriesLinkedList);
//    }
//
//    private void showRestrictedPSlots(LinkedList<ParkingSlot> parkingSlots) {
//        restrictedPSlotsChart.getData().clear();
//        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
//        //series.setName(Complaint.topicToString(topic));
//        seriesLinkedList.add(series);
//
//        //need to implement a date attribute for EditParkingSlots feature (RESTRICTED)!!!
//        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
//            int numOfRestPSl = 0;
//            for (ParkingSlot parkingSlot : parkingSlots) {
//                if (parkingSlot.getStatus().equals(ParkingSlot.Status.RESTRICTED))
//                    numOfRestPSl += 1;
//            }
//
//            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfRestPSl));
//            salesNum.setText(String.valueOf(numOfRestPSl));
//        }
//
//        restrictedPSlotsChart.getData().addAll(seriesLinkedList);
//    }
//    /**
//     * the showChart function gets all relevant complaints, then sorting them by customer type and
//     * displays it on the XYChart
//     */
//    private void showComplaints(LinkedList<Complaint> complaints) {
//        complaintChart.getData().clear();
//        LinkedList<XYChart.Series<String, Number>> seriesLinkedList = new LinkedList<XYChart.Series<String, Number>>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
//        //series.setName(Complaint.topicToString(topic));
//        seriesLinkedList.add(series);
//
//        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
//            int numOfComp = 0;
//            for (Complaint complaint : complaints) {
//                if (dateAreEqual(dateToLocalDate(complaint.getDate()), date))
//                    numOfComp += 1;
//            }
//
//            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(date)), numOfComp));
//        }
//        complaintChart.getData().addAll(seriesLinkedList);
//    }
//
//    /**
//     * @return if the time interval data filled validly
//     */
//
//    public boolean isInvalid() {
//        return toDate.isDisabled() || toDate.getValue() == null;
//    }
//}
