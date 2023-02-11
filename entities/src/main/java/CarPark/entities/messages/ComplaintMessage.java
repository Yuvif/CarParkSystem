package CarPark.entities.messages;

import CarPark.entities.Complaint;
import CarPark.entities.Customer;
import CarPark.entities.Employee;
import CarPark.entities.Parkinglot;

import java.util.LinkedList;
import java.util.List;


public class ComplaintMessage extends Message {
    private String parkinglotId;
    private Parkinglot parkinglot;
    public RequestType request_type;
    public ResponseType response_type;
    public Complaint complaint2handle;
    public Customer current_customer;
    //public Employee current_employee;
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

//        assert this.complaints != null;
//        complaints.add(complaint);  // only if its a case of creating a new complaint- not for open one to handle
        assert this.complaints2Rep != null;
//        assert this.complaint2handle != null;
//        this.complaints2Rep.add(complaint);
        this.complaint2handle = complaint;
    }


    public ComplaintMessage(MessageType messageType, RequestType request_type, Complaint complaint, int amount, Customer customer) {
        super(messageType);
        this.request_type = request_type;
        this.complaint2handle = complaint;
        this.amount = amount;
        this.current_customer = customer;

    }

    public ComplaintMessage(MessageType message_type,RequestType request_type, String parkinglot) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglot;

    }


    public ComplaintMessage(MessageType message_type, ResponseType response_type, LinkedList<Complaint> complaints,  Customer current_user) {
        super(message_type);
        this.response_type = response_type;
        this.complaints = complaints;
        this.complaints2Rep = complaints;
    }

    public ComplaintMessage(MessageType request, RequestType response_type, Customer current_user) {
        super(request);
        this.request_type = response_type;
        this.current_customer = current_user;
    }

    public ComplaintMessage(MessageType request, RequestType response_type, Complaint complaint, Customer current_user) {
        super(request);
        this.request_type = response_type;
        this.complaint2handle = complaint;
        this.current_customer = current_user;
    }


    public enum RequestType {
        GET_ALL_COMPLAINTS,
        CREATE_NEW_COMPLAINT,
        COMPENSATE_COMPLAINT,
        GET_MY_COMPLAINTS,
        GET_OPEN_COMPLAINT,
        GET_INSPECTED_COMPLAINT
    }

    public enum ResponseType {
        SET_ALL_COMPLAINTS,
        COMPLAINT_SUBMITTED,
        COMPLAINT_CLOSED,
        SET_MY_COMPLAINTS,
        SET_DISPLAY_COMPLAINT,
        SET_INSPECTED_COMPLAINT
    }


}