package CarPark.entities.messages;

import CarPark.entities.Order;
import CarPark.entities.Parkinglot;

import java.util.List;

public class OrderMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public Order newOrder;
    public List<Parkinglot> parkingList;

    public enum RequestType {
        CREATE_NEW_ORDER
    }

    public enum ResponseType {
        ORDER_SUBMITTED
    }

    public OrderMessage(MessageType message_type, RequestType request_type, Order order) {
        super(message_type);
        this.request_type = request_type;
        this.newOrder = order;
    }
}
