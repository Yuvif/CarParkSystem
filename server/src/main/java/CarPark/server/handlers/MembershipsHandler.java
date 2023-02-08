package CarPark.server.handlers;

import CarPark.entities.Customer;
import CarPark.entities.Membership;
import CarPark.entities.Price;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.MembershipMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MembershipsHandler extends MessageHandler{

    private MembershipMessage class_message;

    public MembershipsHandler(Message msg, Session session, ConnectionToClient client)
    {
        super(msg, session, client);
        this.class_message = (MembershipMessage) this.message;
    }

    private List<Membership> getMembershipList() throws Exception
    {
        CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
        query.from(Membership.class);
        List<Membership> data = session.createQuery(query).getResultList();
        return data;
    }

    private void createMembership() throws Exception
    {
        Membership newMembership = class_message.newMembership;
        List<Membership> existingMemberships = getMembershipList();

        for (Membership membership : existingMemberships)
        {
            if (membership.getCarId() == newMembership.getCarId() && membership.getCustomerId() == newMembership.getCustomerId())
            {
                class_message.response_type = MembershipMessage.ResponseType.REGISTRATION_FAILED;
                break;
            }
        }

        if(class_message.response_type != MembershipMessage.ResponseType.REGISTRATION_FAILED)
        {
            Random rand = new Random();
            newMembership.setMembershipId(rand.nextInt(99999 + 1 - 10000) + 10000); //generate 5 digit membership number
            class_message.newMembership = newMembership;
            class_message.response_type = MembershipMessage.ResponseType.REGISTRATION_SUCCEEDED;
            calculateMembershipsPrice();
            session.save(newMembership);
            session.flush();
        }
    }

    //find the number of cars of the user that is making a new full membership
    private int getNumberOfCars(String customerId) throws Exception
    {
        List<Membership> membershipList = getMembershipList();
        int carsCnt = 0;

        for(Membership membership : membershipList)
        {
            if(membership.getCustomerId().equals(customerId))
            {
                carsCnt++;
            }
        }
        return carsCnt;
    }


    private void updateThePriceForMultipleCarsMembership(Membership membership, int numOfCars) throws Exception
    {
        String hql = "UPDATE Membership SET membershipsPrice = :price WHERE customerId = :customerID";
        Query query = session.createQuery(hql);
        query.setParameter("price", membership.getMembershipsPrice());
        query.setParameter("customerID", membership.getCustomerId());
        query.executeUpdate();
    }

    private double makeQueryOrderedParkingPrice()
    {
        String hql = "FROM Price WHERE parkingType = :type";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Casual ordered parking");
        Price price = (Price) query.uniqueResult();
        return price.getPrice();
    }

    //the query for routine membership with one car
    private double makeQueryRoutineMembershipPrice()
    {
        String hql = "FROM Price WHERE parkingType = :type";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Monthly subscriber one car");
        Price price = (Price) query.uniqueResult();
        double hours = price.getHoursOfParking();
        return hours * makeQueryOrderedParkingPrice();
    }

    //the query for routine membership with few cars
    private double makeQueryRoutineMembershipMultipleCarsPrice()
    {
        String hql = "FROM Price WHERE parkingType = :type";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Monthly subscriber few cars");
        Price price = (Price) query.uniqueResult();
        double hours = price.getHoursOfParking();

        return hours * makeQueryOrderedParkingPrice();
    }

    //the query for full membership price
    private double makeQueryFullMembershipPrice()
    {
        String hql = "FROM Price WHERE parkingType = :type";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Premium monthly subscriber");
        Price price = (Price) query.uniqueResult();
        double hours = price.getHoursOfParking();

        return hours * makeQueryOrderedParkingPrice();
    }


    private void calculateMembershipsPrice() throws Exception
    {
        Membership membership = class_message.newMembership;
        int numOfCars = getNumberOfCars(membership.getCustomerId());

       if(Objects.equals(membership.getMembershipType(), "Routine Membership") && numOfCars == 0)
       {
           membership.setMembershipsPrice(makeQueryRoutineMembershipPrice());
       }
       else if(Objects.equals(membership.getMembershipType(), "Routine Membership") && numOfCars >= 1)
       {
           membership.setMembershipsPrice(makeQueryRoutineMembershipMultipleCarsPrice());
           if(numOfCars == 1)
           {
               updateThePriceForMultipleCarsMembership(membership, numOfCars);
           }
       }
       else //price for full membership
       {
           membership.setMembershipsPrice(makeQueryFullMembershipPrice());
       }
        Customer current_customer = session.get(Customer.class,class_message.current_customer.getId());
        current_customer.addToBalance(membership.getMembershipsPrice());
    }

    @Override
    public void handleMessage() throws Exception
    {
        switch (class_message.request_type) {
            case REGISTER:
                createMembership();
                break;
            case GET_MY_MEMBERSHIPS:
                getMyMemberships();
                break;
        }
    }

    public void getMyMemberships() throws Exception {
        String hql = "FROM Membership WHERE customerId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        class_message.memberships = query.getResultList();
        class_message.response_type= MembershipMessage.ResponseType.SEND_TABLE;
    }


}
