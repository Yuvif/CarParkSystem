package CarPark.entities.messages;

import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;

import java.util.List;

public class ParkingLotMapMessage extends Message{
    public RequestType request_type;
    public ResponseType response_type;
    public String pl_name;
    public List<ParkingSlot> parkingSlots;
    public List<Parkinglot> parkingLots;
    public int rows;

    public enum RequestType {
        GET_ROW,
        GET_PARKING_SLOTS,
        SHOW_PARKING_LOT_MAP,
        GET_PARKING_LOTS
    }

    public enum ResponseType {
        SET_ROW,
        SEND_PARKING_SLOTS,
        SEND_PARKING_LOT_MAP,
        SET_PARKING_LOTS
    }

    public ParkingLotMapMessage(MessageType message_type, RequestType request_type, String plName) {
        super(message_type);
        this.request_type = request_type;
        this.pl_name = plName;
    }

    public ParkingLotMapMessage(MessageType message_type, RequestType request_type, List<ParkingSlot> parkingSlots) {
        super(message_type);
        this.request_type = request_type;
        this.parkingSlots = parkingSlots;
    }
}
