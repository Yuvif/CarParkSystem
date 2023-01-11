package CarPark.client.events;

import CarPark.entities.Parkinglot;

import java.util.List;

public class ParkingListEvent {
    public List<Parkinglot> table;

    public ParkingListEvent(List<Parkinglot> parkingList) {
        table = parkingList;
    }

}
