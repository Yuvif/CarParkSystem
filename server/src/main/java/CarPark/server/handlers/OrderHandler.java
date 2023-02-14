package CarPark.server.handlers;

import CarPark.entities.*;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.OrderMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
            case GET_ORDERS_TABLE:
                getMyOrdersList();
                break;
            case CANCEL_ORDER:
                cancelOrder();
                break;
            case GET_SELECTED_ORDERS:
                class_message.ordersList = getSelectedOrders();
                class_message.response_type = OrderMessage.ResponseType.SET_SELECTED_ORDERS;
//                class_message.ordersList.removeIf(order -> order.getParkingLotId().equals(class_message.parking_lot_id)
//                        || order.getDate().compareTo(class_message.from) < 0 || order.getDate().compareTo(class_message.to) > 0
//                        || order.getStatus() == Order.Status.APPROVED);
        }
    }

    private void createOrder() throws Exception
    {
        Order newOrder = class_message.Order;
        newOrder.setOrdersPrice(calculateOrdersPrice());
        class_message.current_customer.addOrder(newOrder);
        Customer current_customer = session.get(Customer.class,class_message.current_customer.getId());
        current_customer.addToBalance(newOrder.getOrdersPrice());
        session.save(newOrder);
        session.flush();
    }

    private double calculateOrdersPrice() throws Exception
    {
        Order newOrder = class_message.Order;
        String membershipType = findAndGetMembershipType(newOrder);

        if(!Objects.equals(membershipType, "Full Membership") && !Objects.equals(membershipType, "Routine Membership"))
        {
            String routinePL = class_message.Order.getParkingLotId();
            String hql1 = "FROM Parkinglot WHERE name = : plName";
            Query query1 = session.createQuery(hql1);
            query1.setParameter("plName", routinePL);
            Parkinglot parkinglot = (Parkinglot) query1.uniqueResult();

            String hql2 = "FROM Price WHERE parkingType = :type and parkinglot = : PL";
            Query query = session.createQuery(hql2);
            query.setParameter("type", "Casual ordered parking");
            query.setParameter("PL", parkinglot);
            Price price = (Price) query.uniqueResult();
            double pricePH = price.getPrice();
            double time = calculateNumOfParkingHours(newOrder.getArrivalTime(), newOrder.getEstimatedLeavingTime());
            return time * pricePH;
        }
        return 0.0;
    }

    private String findAndGetMembershipType(Order newOrder) throws Exception
    {
        List<Membership> memberships = getMembershipsList();

        for(Membership membership : memberships)
        {
            if(membership.getCustomerId().equals(newOrder.getCustomerId()) &&
                    membership.getCarId() == newOrder.getCarId())
            {
                return membership.getMembershipType();
            }
        }
        return "Not Found";
    }

    private List<Membership> getMembershipsList() throws Exception
    {
        CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
        query.from(Membership.class);
        return session.createQuery(query).getResultList();
    }

    private double calculateNumOfParkingHours(LocalDateTime arrive, LocalDateTime leave)
    {
        double hours =  ChronoUnit.HOURS.between(arrive, leave);
        double minutes = ChronoUnit.MINUTES.between(arrive, leave);
        return hours + (minutes - hours * 60) / 60;
    }


    private void getMyOrdersList() throws Exception
    {
        String hql = "FROM Order WHERE customerId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        class_message.ordersList = query.getResultList();
        class_message.response_type= OrderMessage.ResponseType.SET_ORDERS_TABLE;
    }

    //calculate the credit of the customer that cancels his order
    private double calculateCancellationCredit(LocalDateTime arrivalTime)
    {
        LocalDateTime cancelTime = LocalDateTime.now();
        long differenceInMinutes = ChronoUnit.MINUTES.between(cancelTime, arrivalTime);
        if( differenceInMinutes >= 3 * 60)
        {
            return class_message.Order.getOrdersPrice() * -0.9;
        }
        else if(differenceInMinutes > 60)
        {
            return class_message.Order.getOrdersPrice() * -0.5;
        }
        else
        {
            return class_message.Order.getOrdersPrice() * -0.1;
        }
    }

    private void cancelOrder() throws Exception
    {
        double credit = calculateCancellationCredit(class_message.Order.getArrivalTime());
        Customer current_customer = session.get(Customer.class,class_message.current_customer.getId());
        current_customer.addToBalance(credit);
        String hql = "DELETE FROM Order WHERE carId = :carNum AND parkingLot = :p_l AND arrivalTime = :arrival AND estimatedLeavingTime = : leaving";
        javax.persistence.Query query = session.createQuery(hql);
        query.setParameter("carNum",class_message.Order.getCarId());
        query.setParameter("p_l",class_message.Order.getParkingLotId());
        query.setParameter("arrival",class_message.Order.getArrivalTime());
        query.setParameter("leaving",class_message.Order.getEstimatedLeavingTime());
        if(query.executeUpdate() > 0)
        {
            class_message.response_type = OrderMessage.ResponseType.ORDER_CANCELED;
            class_message.credit = credit * (-1);
        }
    }

    private LinkedList<Order> getSelectedOrders() throws Exception {
        //generateOrders();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> orderQuery = builder.createQuery(Order.class);
        orderQuery.from(Order.class);
        List<Order> data = session.createQuery(orderQuery).getResultList();
        LinkedList<Order> res= new LinkedList<Order>();
        for(Order c: data)
        {
            res.add(c);
        }
        return res;
    }
}