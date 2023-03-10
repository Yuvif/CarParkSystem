package CarPark.client.controllers.Employee.Manager;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.client.controllers.AbstractReports;
import CarPark.entities.*;
import CarPark.entities.messages.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

        Employee current_employee = (Employee) SimpleClient.getCurrent_user();
        switch (current_employee.getWorkersRole()) {
            case "Manager":
                Manager current_manager = (Manager) current_employee;
                String parkingLotName = current_manager.getParkinglot().getName();
                plPicker.setPromptText(parkingLotName);
                plPicker.setDisable(true);
                break;

            case "Customer Service Worker":
                break;
        }

    }
    /**
     * makeReport function activates if pressing the make report button, first it makes
     * sure that a valid time interval and store has been inserted, and then sends to the server
     * a request for all orders and complaints that are relevant for the request.
     */

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Employee current_employee = (Employee) SimpleClient.getCurrent_user();
        switch (current_employee.getWorkersRole()) {
            case "Manager" -> SimpleChatClient.setRoot("ParkingLotManagerPage");
            case "Customer Service Worker" -> SimpleChatClient.setRoot("CustomerServicePage");
        }
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
                    OrderMessage.RequestType.GET_SELECTED_ORDERS, parkingLotName, from, to );
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
        switch (new_message.response_type) {
            case SET_SELECTED_ORDERS -> Platform.runLater(() -> showOrders(new_message.customerList));
        }
    }
    @Subscribe
    public void newResponse(PullParkingSlotsMessage new_message) {
        switch (new_message.response_type) {
            case SET_PARKING_SLOTS_REP -> Platform.runLater(()-> showRestrictedPSlots(new_message.parkingSlots));
        }
    }
    @Subscribe
    public void newResponse(ComplaintMessage new_message) {
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

    private void showOrders(List<Customer> customers) {

        for (LocalDate date : getDatesBetween(fromDate.getValue(), toDate.getValue())) {
            int numOfCustomers = 0;
            int  num_of_orders_from_memberships = 0;
            for (Customer customer : customers) {
                int i;
                for (i = 0; i<customer.getOrderList().size(); i++)
                {
                    if (dateAreEqual(dateToLocalDate(customer.getOrderList().get(i).getDate()), date) &&
                            customer.getOrderList().get(i).getParkingLotId().equals(plPicker.getPromptText())) {
                        if (customer.getMemberships().size()>0) {
                            num_of_orders_from_memberships += 1;
                        } else {
                            numOfCustomers += 1;
                        }
                    }
                }

            }


            ordersLabel.setText(String.valueOf(numOfCustomers +  num_of_orders_from_memberships));
            customersLabel.setText(String.valueOf(numOfCustomers));
            membershipsLabel.setText(String.valueOf( num_of_orders_from_memberships));

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
            for (ParkingSlot parkingSlot : parkingSlots) {
                int numOfRestPSl = 0;
                if (parkingSlot.getSpotStatus().equals(ParkingSlot.Status.RESTRICTED)
                        &&
                       parkingSlot.getParkinglot().getName().equals(plPicker.getPromptText()))
                    numOfRestPSl += 1;

            series.getData().add(new XYChart.Data<>(formatter.format(localDateToDate(LocalDate.now())), numOfRestPSl));
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
                if (dateAreEqual(dateToLocalDate(complaint.getDate()), date)&&
                       complaint.getParkinglot().getName().equals(plPicker.getPromptText()))
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
