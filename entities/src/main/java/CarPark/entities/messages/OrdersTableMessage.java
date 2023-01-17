package CarPark.entities.messages;

import CarPark.entities.Order;
import java.util.List;

public class OrdersTableMessage extends Message {

    public RequestType request_type;
    public ResponseType response_type;
    public List<Order> ordersList;
    public Order canceledOrder;


    public enum RequestType {
        GET_ORDERS_TABLE,
        CANCEL_ORDER
    }

    public enum ResponseType {
        SET_ORDERS_TABLE,
        ORDER_CANCELED
    }

    public OrdersTableMessage(MessageType message_type, RequestType request_type)
    {
        super(message_type);
        this.request_type = request_type;
    }

    public OrdersTableMessage(MessageType message_type, RequestType request_type, Order canceledOrder)
    {
        super(message_type);
        this.request_type = request_type;
        this.canceledOrder = canceledOrder;
    }
}
