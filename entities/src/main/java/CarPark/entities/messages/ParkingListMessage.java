package CarPark.entities.messages;

import CarPark.entities.Parkinglot;

import java.util.List;

public class ParkingListMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public List<Parkinglot> parkingList;


    public enum RequestType {
        GET_ALL_PARKING_LOTS,
        ADD_NEW_PARKING_LOT
    }

    public enum ResponseType {
        SET_ALL_PARKING_LOTS
    }

    public ParkingListMessage(MessageType message_type, RequestType request_type) {
        super(message_type);
        this.request_type = request_type;
    }


}
