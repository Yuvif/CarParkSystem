package CarPark.entities.messages;
import CarPark.entities.ParkingSlot;

import java.util.Date;
import java.util.LinkedList;

public class PullParkingSlotsMessage extends Message {

    public RequestType request_type;
    public ResponseType response_type;
    //public Parkinglot parkinglot;
    public String parkinglotId;
    public Date from;
    public Date to;
    //    public LinkedList<Complaint> complaints;
    public LinkedList<ParkingSlot> parkingSlots;

    public PullParkingSlotsMessage(MessageType message_type, PullParkingSlotsMessage.RequestType requestType) {
        super(message_type);
        this.request_type = requestType;
    }
    public PullParkingSlotsMessage(MessageType message_type, PullParkingSlotsMessage.RequestType request_type, String parkinglotId, Date from, Date to) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglotId;
        this.from = from;
        this.to = to;

    }
    public PullParkingSlotsMessage(MessageType message_type, PullParkingSlotsMessage.ResponseType response_type, LinkedList<ParkingSlot> parkingslots) {
        super(message_type);
        this.response_type = response_type;
        this.parkingSlots = parkingslots;
    }

    public enum RequestType {
        //        GET_SELECTED_COMPLAINTS,
        GET_PARKING_SLOTS_REP
    }

    public enum ResponseType {
        //        SET_SELECTED_COMPLAINTS,
        SET_PARKING_SLOTS_REP
    }

}