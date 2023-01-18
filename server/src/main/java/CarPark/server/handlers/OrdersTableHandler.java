package CarPark.server.handlers;

import CarPark.entities.Order;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.OrdersTableMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class OrdersTableHandler extends MessageHandler{

    private final OrdersTableMessage class_message;

    public OrdersTableHandler(Message msg, Session session, ConnectionToClient client)
    {
        super(msg, session, client);
        this.class_message = (OrdersTableMessage) this.message;;
    }

    private List<Order> getOrdersList() throws Exception
    {
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        query.from(Order.class);
        List<Order> data = session.createQuery(query).getResultList();
        return data;
    }

    //calculate the credit of the customer that cancels his order
    private double calculateCancellationCredit(LocalDateTime arrivalTime)
    {
        LocalDateTime cancelTime = LocalDateTime.now();
        long differenceInMinutes = ChronoUnit.MINUTES.between(cancelTime, arrivalTime);
        if( differenceInMinutes >= 3 * 60)
        {
            return class_message.canceledOrder.getOrdersPrice() * 0.9;
        }
        else if(differenceInMinutes > 60)
        {
            return class_message.canceledOrder.getOrdersPrice() * 0.5;
        }
        else
        {
            return class_message.canceledOrder.getOrdersPrice() * 0.1;
        }
    }

    private void cancelOrder() throws Exception
    {

        String hql = "DELETE FROM Order WHERE carId = :carNum AND parkingLot = :p_l AND arrivalTime = :arrival AND estimatedLeavingTime = : leaving";
        Query query = session.createQuery(hql);
        query.setParameter("carNum",class_message.canceledOrder.getCarId());
        query.setParameter("p_l",class_message.canceledOrder.getParkingLotId());
        query.setParameter("arrival",class_message.canceledOrder.getArrivalTime());
        query.setParameter("leaving",class_message.canceledOrder.getEstimatedLeavingTime());
        if(query.executeUpdate() > 0)
        {
            //I need the customer here to update his balance (!!!!!!!)

            class_message.response_type = OrdersTableMessage.ResponseType.ORDER_CANCELED;
        }
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_ORDERS_TABLE:
                class_message.ordersList = getOrdersList();
                class_message.response_type = OrdersTableMessage.ResponseType.SET_ORDERS_TABLE;
                break;
            case CANCEL_ORDER:
                cancelOrder();
                break;
        }
    }


}
