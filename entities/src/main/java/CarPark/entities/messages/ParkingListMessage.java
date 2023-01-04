package CarPark.entities.messages;

import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import org.hibernate.type.ObjectType;

import java.util.List;

public class ParkingListMessage extends Message {
    public RequestType request_type;
    public List<Parkinglot> parkingList;

    public enum RequestType{
        GET_ALL_PARKING_LOTS,
        ADD_NEW_PARKING_LOT
    }

    public ParkingListMessage(MessageType message_type, RequestType request_type) {
        super(message_type);
        this.request_type = request_type;
    }


}
