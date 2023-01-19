package CarPark.entities.messages;
import CarPark.entities.Complaint;
import CarPark.entities.Order;
import CarPark.entities.ParkingSlot;

import java.util.Date;
import java.util.LinkedList;

public class PullOrdersMessage extends Message  {

    public RequestType request_type;
    public ResponseType response_type;
    //public Parkinglot parkinglot;
    public String parkinglotId;
    public Date from;
    public Date to;
    public LinkedList<Order> orders;


    public PullOrdersMessage(MessageType message_type, PullOrdersMessage.RequestType requestType) {
        super(message_type);
        this.request_type = requestType;
    }
    public PullOrdersMessage(MessageType message_type, PullOrdersMessage.RequestType request_type, String parkinglotId, Date from, Date to) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglotId;
        this.from = from;
        this.to = to;
    }
    public PullOrdersMessage(MessageType message_type, PullOrdersMessage.ResponseType response_type, LinkedList<Order> orders) {
        super(message_type);
        this.response_type = response_type;
        this.orders = orders;
    }

    public enum RequestType {
        GET_SELECTED_ORDERS
    }

    public enum ResponseType {
        SET_SELECTED_ORDERS
    }

}
