package CarPark.client.controllers.Customer;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.Order;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.OrderMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;

import static CarPark.client.controllers.Controller.sendAlert;

public class MyOrdersController {
    @FXML
    private TableColumn<Order, LocalDateTime> arrivalCol;
    @FXML
    private Label userName;
    @FXML
    private TableColumn<Order, Void> cancelCol;
    @FXML
    private TableColumn<Order, Integer> carIdCol;
    @FXML
    private TableColumn<Order, LocalDateTime> estLeavingTimeCol;
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, String> parkingLotCol;


    @FXML
    void initialize() throws IOException
    {
        EventBus.getDefault().register(this);
        userName.setText(SimpleClient.getCurrent_user().getFirstName());
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        parkingLotCol.setCellValueFactory(new PropertyValueFactory<>("parkingLotId"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        estLeavingTimeCol.setCellValueFactory(new PropertyValueFactory<>("estimatedLeavingTime"));
        OrderMessage msg = new OrderMessage(Message.MessageType.REQUEST, OrderMessage.RequestType.GET_ORDERS_TABLE,(Customer)SimpleClient.getCurrent_user());
        try {
            SimpleClient.getClient().sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initialize prices table from server
    @Subscribe
    public void newResponse(OrderMessage new_message)
    {
        switch (new_message.response_type) {
            case SET_ORDERS_TABLE -> {
                ordersTable.setItems(FXCollections.observableArrayList(new_message.ordersList));
                Platform.runLater(this::addButtonToTable);
            }
            case ORDER_CANCELED -> {
                deleteRow();
                sendAlert("Your Order Is Canceled!"+
                                "\nA Compensation of " + new_message.credit + "₪ was made",
                        "Cancellation Confirmed", Alert.AlertType.INFORMATION);
            }
        }
    }


    //add cancel button to every order
    private void addButtonToTable() {
        cancelCol = new TableColumn("");

        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Cancel");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Order order = getTableRow().getItem();
                            OrderMessage msg = new OrderMessage(Message.MessageType.REQUEST,
                                    OrderMessage.RequestType.CANCEL_ORDER, order,(Customer) SimpleClient.getCurrent_user());
                            try {
                                SimpleClient.getClient().sendToServer(msg);
                                ordersTable.getItems().remove(getTableRow().getItem()); //remove the order from table
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        cancelCol.setCellFactory(cellFactory);
        ordersTable.getColumns().add(cancelCol);
    }


    public void deleteRow()
    {

    }

    @FXML
    private void myMemberships(ActionEvent event) throws IOException {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("MembershipsView");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void newMembership(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("RegisterAsMember");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void myOrders(ActionEvent event)
    {
        Platform.runLater(()->
        {
            try {
                SimpleChatClient.setRoot("OrdersTable");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    void createNewOrder(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("CreateOrder");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void newComplaint(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("ComplaintSubmission");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void myComplaints(ActionEvent event) throws IOException{
        Platform.runLater(() -> {
            try {
                SimpleChatClient.setRoot("MyComplaints");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
