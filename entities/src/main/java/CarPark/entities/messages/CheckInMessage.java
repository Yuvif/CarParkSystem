package CarPark.entities.messages;

import CarPark.entities.CheckedIn;
import CarPark.entities.Complaint;
import CarPark.entities.Customer;

import java.util.LinkedList;

public class CheckInMessage extends Message {
    public CheckInMessage.RequestType request_type;
    public CheckInMessage.ResponseType response_type;
    public CheckedIn checkedIn;
    public String alternativeParkingLot;
    public String selectedParkingLot;

    public enum RequestType {
        CHECK_ME_IN_GUEST
    }

    public enum ResponseType {
        CHECKED_IN_GUEST,
        PARKING_LOT_IS_FULL;
    }

    public CheckInMessage(MessageType message_type, CheckInMessage.RequestType request_type, CheckedIn checkedIn) {
        super(message_type);
        this.request_type = request_type;
        this.checkedIn = checkedIn;
    }

    public CheckInMessage(MessageType message_type, CheckInMessage.ResponseType response_type) {
        super(message_type);
        this.response_type = response_type;
    }

    public void setAlternativeParkingLot(String alternativeParkingLot){
        this.alternativeParkingLot = alternativeParkingLot;
    }
}