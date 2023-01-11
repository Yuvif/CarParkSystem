package CarPark.server.handlers;

import CarPark.entities.Order;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.OrderMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class OrderHandler extends MessageHandler {

    private final OrderMessage class_message;

    public OrderHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (OrderMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case CREATE_NEW_ORDER:
                createOrder();
                class_message.response_type = OrderMessage.ResponseType.ORDER_SUBMITTED;
                break;
        }
    }

    private void createOrder() {
        Order newOrder = class_message.newOrder;
        session.save(newOrder);
        session.flush();
    }

    private List<Order> getOrderList() throws Exception {
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        query.from(Order.class);
        List<Order> data = session.createQuery(query).getResultList();
        return data;
    }
}
