package CarPark.client.events;

import CarPark.entities.Order;
import CarPark.entities.Parkinglot;

import java.util.List;

public class OrderEvent {
    public List<Order> table;

    public OrderEvent(List<Order> orderList) {
        table = orderList;
    }
}
