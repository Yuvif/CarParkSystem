package CarPark.client.events;

import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;

import java.util.List;

public class ParkingListMessageEvent extends MessageEvent{
    public List<Parkinglot> table;
    public ParkingListMessageEvent(ParkingListMessage message) {
        super(message);
        table = message.parkingList;
    }

}
