package CarPark.entities.messages;

import CarPark.entities.Complaint;

import java.util.List;

public class ComplaintMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public List<Complaint> complaints;

    public ComplaintMessage(MessageType message_type) {
        super(message_type);
    }

    public enum RequestType {
        GET_ALL_COMPLAINTS,
        CREATE_NEW_COMPLAINT
    }

    public enum ResponseType {
        ALL_COMPLAINTS,
        COMPLAINT_SUBMITTED
    }

    public ComplaintMessage(MessageType message_type, RequestType request_type, List<Complaint> complaints) {
        super(message_type);
        this.request_type = request_type;
        this.complaints = complaints;
    }
}
