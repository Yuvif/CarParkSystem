package CarPark.server.handlers;

import CarPark.entities.Membership;
import CarPark.entities.Order;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.OrderMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        }
    }

    private void createOrder() throws Exception
    {
        Order newOrder = class_message.newOrder;
        newOrder.setOrdersPrice(calculateOrdersPrice());
        class_message.current_customer.addOrder(newOrder);
        class_message.current_customer.addToBalance(newOrder.getOrdersPrice());
        session.save(newOrder);
        session.flush();
    }

    private double calculateOrdersPrice() throws Exception
    {
        Order newOrder = class_message.newOrder;
        String membershipType = findAndGetMembershipType(newOrder);

       if(!Objects.equals(membershipType, "Full Membership") && !Objects.equals(membershipType, "Routine Membership"))
        {
            String hql = "FROM Price WHERE parkingType = :type";
            Query query = session.createQuery(hql);
            query.setParameter("type", "Casual ordered parking");
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
            if(membership.getCustomerId() == newOrder.getCustomerId() &&
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
}
