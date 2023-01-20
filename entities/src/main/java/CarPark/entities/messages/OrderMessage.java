package CarPark.entities.messages;

import CarPark.entities.Customer;
import CarPark.entities.Order;

import java.util.Date;
import java.util.List;

public class OrderMessage extends Message {
    //Customer usage
    public Customer current_customer;
    public RequestType request_type;
    public ResponseType response_type;
    public List<Order> ordersList;
    public Order Order;

    //For reports
    public String parking_lot_id;
    public Date from;
    public Date to;

    public OrderMessage(MessageType request, RequestType createNewOrder, Order order, Customer current_customer) {
        super(request);
        this.request_type = createNewOrder;
        this.Order = order;
        this.current_customer = current_customer;
    }

    public OrderMessage(MessageType request, RequestType getSelectedOrders, String parkinglot, Date from, Date to) {
        super(request);
        this.request_type = getSelectedOrders;
        this.parking_lot_id = parkinglot;
        this.from = from;
        this.to = to;
    }

    public OrderMessage(MessageType request, RequestType getOrdersTable, Customer current_user) {
        super(request);
        this.request_type= getOrdersTable;
        this.current_customer = current_user;
    }

    public enum RequestType {
        GET_ORDERS_TABLE,
        CANCEL_ORDER,
        GET_SELECTED_ORDERS,
        CREATE_NEW_ORDER
    }

    public enum ResponseType {
        SET_ORDERS_TABLE,
        ORDER_CANCELED,
        SET_SELECTED_ORDERS,
        ORDER_SUBMITTED

    }

    public OrderMessage(MessageType message_type, RequestType request_type)
    {
        super(message_type);
        this.request_type = request_type;
    }

    public OrderMessage(MessageType message_type, RequestType request_type, Order canceledOrder)
    {
        super(message_type);
        this.request_type = request_type;
        this.Order = canceledOrder;
    }
}
