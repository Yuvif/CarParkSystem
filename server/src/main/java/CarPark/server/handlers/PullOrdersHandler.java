package CarPark.server.handlers;

import CarPark.entities.Order;
import CarPark.entities.ParkingSlot;
import CarPark.entities.messages.PullOrdersMessage;
import CarPark.entities.messages.PullParkingSlotsMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.LinkedList;
import java.util.List;

public class PullOrdersHandler extends MessageHandler {

    private final PullOrdersMessage class_orders_message;

    public PullOrdersHandler(PullOrdersMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_orders_message = (PullOrdersMessage)this.message;
    }
    @Override
    public void handleMessage() throws Exception {

        switch (class_orders_message.request_type) {
            case GET_SELECTED_ORDERS:
                class_orders_message.orders = getOrders();
                class_orders_message.response_type = PullOrdersMessage.ResponseType.SET_SELECTED_ORDERS;
                class_orders_message.orders.removeIf(order -> order.getParkingLotId().equals(class_orders_message.parkinglotId)
                        || order.getDate().compareTo(class_orders_message.from) < 0 || order.getDate().compareTo(class_orders_message.to) > 0
                        || order.getStatus());
                break;
        }

    }

    private LinkedList<Order> getOrders() throws Exception {
        //generateOrders();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> orderQuery = builder.createQuery(Order.class);
        orderQuery.from(Order.class);
        List<Order> orders = session.createQuery(orderQuery).getResultList();
        return new LinkedList<Order>(orders);
    }

}
