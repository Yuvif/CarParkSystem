package CarPark.entities.messages;

import CarPark.entities.Customer;

public class CheckOutMessage extends Message{
    public Customer current_customer;
    public long carNumber;
    public RequestType request_type;
    public ResponseType response_type;

    public CheckOutMessage(MessageType message_type, RequestType request_type, Customer current_customer) {
        super(message_type);
        this.request_type = request_type;
        this.current_customer = current_customer;
    }

    public CheckOutMessage(MessageType message_type, RequestType request_type, long carNumber) {
        super(message_type);
        this.request_type = request_type;
        this.carNumber = carNumber;
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
