package CarPark.entities.messages;

import CarPark.entities.Customer;

public class CheckOutMessage extends Message{
    public Customer current_customer;
    public String userId;
    public double payment;
    public String carNumber;
    public boolean hasAnOrder;
    public RequestType request_type;
    public ResponseType response_type;

    public CheckOutMessage(MessageType message_type, RequestType request_type, Customer current_customer, String carNumber,
                           double payment, boolean hasAnOrder) {
        super(message_type);
        this.request_type = request_type;
        this.current_customer = current_customer;
        this.carNumber = carNumber;
        this.payment = payment;
        this.hasAnOrder = hasAnOrder;
    }

    public CheckOutMessage(MessageType message_type, RequestType request_type, String userId, String carNumber, boolean hasAnOrder) {
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
        NO_CHECK_IN,
        CHECKED_OUT_GUEST
    }
}
