package CarPark.client.events;

import CarPark.entities.ParkingLotWorker;
import CarPark.entities.Parkinglot;
import javafx.event.Event;
import javafx.event.EventType;

import java.util.List;

public class ParkingListEvent{
    public List<Parkinglot> table;


    public ParkingListEvent(List<Parkinglot> parkingList) {
        table = parkingList;
    }

}
