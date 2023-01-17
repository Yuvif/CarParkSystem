package CarPark.entities.messages;

import CarPark.entities.Complaint;
import java.util.LinkedList;
import java.util.List;


public class ComplaintMessage extends Message {
    private String parkinglotId;
    public RequestType request_type;
    public ResponseType response_type;
    public Complaint complaint2handle;
    public int amount;
    public List<Complaint> complaints;
    public LinkedList<Complaint> complaints2Rep;

    public ComplaintMessage(MessageType message_type,RequestType request_type) {
        super(message_type);
        this.request_type = request_type;
    }
    public ComplaintMessage(MessageType message_type, RequestType request_type, Complaint complaint) {
        super(message_type);
        this.request_type = request_type;
        assert this.complaints != null;
        this.complaints.add(complaint);
        this.complaints2Rep.add(complaint);
        this.complaint2handle = complaint;
    }

    public ComplaintMessage(MessageType message_type, ComplaintMessage.RequestType request_type, String parkinglot) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglot;

    }
    public ComplaintMessage(MessageType messageType, RequestType request_type, Complaint complaint, int amount) {
        super(messageType);
        this.request_type = request_type;
        this.complaint2handle = complaint;
        this.amount = amount;

    }

    public ComplaintMessage(MessageType message_type, ComplaintMessage.ResponseType response_type, LinkedList<Complaint> complaints) {
        super(message_type);
        this.response_type = response_type;
        this.complaints = complaints;
        this.complaints2Rep = complaints;
    }



    public enum RequestType {
        GET_ALL_COMPLAINTS,
        CREATE_NEW_COMPLAINT,
        COMPENSATE_COMPLAINT,
        NON_COMPENSATE_COMPLAINT

    }

    public enum ResponseType {
        SET_ALL_COMPLAINTS,
        COMPLAINT_SUBMITTED,
        COMPLAINT_CLOSED
    }


}
