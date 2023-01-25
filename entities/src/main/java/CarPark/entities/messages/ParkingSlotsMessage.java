package CarPark.entities.messages;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
//import javafx.collections.ObservableList;
import java.util.List;

public class ParkingSlotsMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public List<Parkinglot> parkingList;
    public String parkinglotId;
    //public ObservableList<ParkingSlot> parkingslots;
    public List<ParkingSlot> parkingSlots;

    public ParkingSlotsMessage(MessageType message_type, ParkingSlotsMessage.RequestType requestType) {
        super(message_type);
        this.request_type = requestType;
    }
    public ParkingSlotsMessage(MessageType message_type, ParkingSlotsMessage.RequestType request_type, String parkinglotId) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglotId = parkinglotId;
    }
    public ParkingSlotsMessage(MessageType message_type, ParkingSlotsMessage.ResponseType response_type, List<ParkingSlot> parkingSlots) {
        super(message_type);
        this.response_type = response_type;
        this.parkingSlots = parkingSlots;
    }


    public enum RequestType {
        GET_ALL_PARKING_LOTS,
        GET_SELECTED_PARKING_SLOTS,
        SET_PARKING_SLOT_STATUS,
    }

    public enum ResponseType {
        SET_ALL_PARKING_LOTS,
        SET_PARKING_SLOTS,
        PARKING_SLOT_EDITED
    }

}