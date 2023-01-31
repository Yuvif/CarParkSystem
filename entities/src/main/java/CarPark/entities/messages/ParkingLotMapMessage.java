package CarPark.entities.messages;

public class ParkingLotMapMessage extends Message{
    public RequestType request_type;
    public ResponseType response_type;

    public enum RequestType {
        SHOW_PARKING_LOT_MAP,
        ARRANGE_MAP
    }

    public enum ResponseType {
        SEND_PARKING_LOT_MAP,
        SEND_ARRANGED_MAP
    }

    public ParkingLotMapMessage(MessageType message_type) {
        super(message_type);
    }
}
