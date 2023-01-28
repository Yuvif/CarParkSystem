package CarPark.entities.messages;

import CarPark.entities.CheckedIn;
import CarPark.entities.Customer;

public class CheckOutMessage extends Message{
    public Customer current_customer;
    public long userId;
    public double payment;
    public int carNumber;
    public boolean hasAnOrder;
    public RequestType request_type;
    public ResponseType response_type;

    public CheckOutMessage(MessageType message_type, RequestType request_type, Customer current_customer, int carNumber,
                           double payment, boolean hasAnOrder) {
        super(message_type);
        this.request_type = request_type;
        this.current_customer = current_customer;
        this.carNumber = carNumber;
        this.payment = payment;
        this.hasAnOrder = hasAnOrder;
    }

    public CheckOutMessage(MessageType message_type, RequestType request_type, long userId, int carNumber, boolean hasAnOrder) {
        super(message_type);
        this.request_type = request_type;
        this.userId = userId;
        this.carNumber = carNumber;
        this.hasAnOrder = hasAnOrder;
    }

    public enum RequestType {
        CHECK_ME_OUT,
        CHECK_ME_OUT_GUEST
    }

    public enum ResponseType {
        CHECKED_OUT,
        CHECKED_OUT_GUEST
    }
}
