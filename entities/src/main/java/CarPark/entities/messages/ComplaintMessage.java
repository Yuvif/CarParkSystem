package CarPark.entities.messages;

import CarPark.entities.Complaint;
import java.util.LinkedList;
import java.util.List;


public class ComplaintMessage extends Message {
    private String parkinglotId;
    public RequestType request_type;
    public ResponseType response_type;
    public List<Complaint> complaints;
    public LinkedList<Complaint> complaints2Rep;

    public ComplaintMessage(MessageType message_type,RequestType request_type) {
        super(message_type);
        this.request_type = request_type;
    }
    public ComplaintMessage(MessageType message_type, RequestType request_type, Complaint complaint) {
        super(message_type);
        this.request_type = request_type;
        //this.complaints.add(complaint);
        this.complaints2Rep.add(complaint);
    }

    public ComplaintMessage(MessageType message_type, ComplaintMessage.RequestType request_type, String parkinglot) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglot;

    }
    public ComplaintMessage(MessageType message_type, ComplaintMessage.ResponseType response_type, LinkedList<Complaint> complaints) {
        super(message_type);
        this.response_type = response_type;
        this.complaints2Rep = complaints;
    }

    public enum RequestType {
        GET_ALL_COMPLAINTS,
        CREATE_NEW_COMPLAINT
    }

    public enum ResponseType {
        ALL_COMPLAINTS,
        COMPLAINT_SUBMITTED
    }


}
