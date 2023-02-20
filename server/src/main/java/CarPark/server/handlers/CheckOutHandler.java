package CarPark.server.handlers;

import CarPark.entities.*;
import CarPark.entities.messages.*;
import CarPark.entities.messages.Message;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CheckOutHandler extends MessageHandler {
    private final CheckOutMessage class_message;

    public CheckOutHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (CheckOutMessage)this.message;
    }

    public CheckedIn findCheckedInGuest()
    {
        String hql = "FROM CheckedIn WHERE personId = :id AND carNumber = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.userId);
        query.setParameter("carId", class_message.carNumber);
        return (CheckedIn)query.uniqueResult();
    }


    public boolean checkLate(LocalDateTime leavingTime, LocalDateTime estimatedLeavingTime)
    {
        return !leavingTime.isBefore(estimatedLeavingTime) && !leavingTime.isEqual(estimatedLeavingTime);
    }

    public double calculateCharges(LocalDateTime arrivalTime, LocalDateTime leavingTime, LocalDateTime estimatedLeavingTime, double pricePH)
    {
        if(!checkLate(leavingTime, estimatedLeavingTime))
        {
            double differenceInMinutes = ChronoUnit.MINUTES.between(arrivalTime, leavingTime);
            double differenceInHours = differenceInMinutes / 60;
            return pricePH * differenceInHours;
        }
        else
        {
            double differenceInMinutes = ChronoUnit.MINUTES.between(arrivalTime, estimatedLeavingTime);
            double differenceInHours = differenceInMinutes / 60;
            double differenceInMinutes_TimeDeviation = ChronoUnit.MINUTES.between(estimatedLeavingTime, LocalDateTime.now());
            double differenceInHours_TimeDeviation = differenceInMinutes_TimeDeviation / 60;
            return  pricePH * differenceInHours + 1.1 * pricePH * differenceInHours_TimeDeviation;
        }
    }

    //calculation of charges for guests
    public double calcGuestCredit()
    {
        CheckedIn checkedInGuest = findCheckedInGuest();
        String hql = "FROM Price WHERE parkingType = :type and parkinglot = : lot";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Casual parking");
        query.setParameter("lot", checkedInGuest.getParkinglot());
        Price price = (Price) query.uniqueResult();
        double pricePH = price.getPrice();
        return calculateCharges(checkedInGuest.getEntryDate(), checkedInGuest.getExitEstimatedDate(), LocalDateTime.now(), pricePH);
    }

    public double calcOrdersCredit()
    {
        CheckedIn checkedInGuest = findCheckedInGuest();
        String hql = "FROM Price WHERE parkingType = :type and parkinglot = : lot";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Casual ordered parking");
        query.setParameter("lot", checkedInGuest.getParkinglot());
        Price price = (Price) query.uniqueResult();
        return price.getPrice();
    }

    public boolean checkCustomersMembership()
    {
        String hql = "FROM Membership WHERE customerId = :id AND carId = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        query.setParameter("carId", class_message.carNumber);
        return (query.uniqueResult() != null);
    }

    public void checkCustomersOrder()
    {
        double additionalCharge = 0.0;
        String hql = "FROM Order WHERE customerId = :id AND carId = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        query.setParameter("carId", class_message.carNumber);
        Order order = (Order) query.uniqueResult();

        if(order != null)
        {
            class_message.hasAnOrder = true;
            //calculate the additional charges for late
            if(checkLate(LocalDateTime.now(), order.getEstimatedLeavingTime()))
            {
                double differenceInMinutes_TimeDeviation = ChronoUnit.MINUTES.between(order.getEstimatedLeavingTime(), LocalDateTime.now());
                double differenceInHours_TimeDeviation = differenceInMinutes_TimeDeviation / 60;
                additionalCharge = 1.1 * calcOrdersCredit() * differenceInHours_TimeDeviation;
                class_message.current_customer.addToBalance(additionalCharge);
            }
        }
    }

    public void calcCustomerCredit()
    {
        if(!checkCustomersMembership())
        {
            checkCustomersOrder();
        }
        else
        {
            class_message.payment = calcGuestCredit();
        }
    }

    public void deleteFromCheckedIn()
    {
        String hql = "FROM CheckedIn WHERE personId = :id AND carNumber = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.userId);
        query.setParameter("carId", class_message.carNumber);
        CheckedIn checkedOut = (CheckedIn)query.uniqueResult();

        String hql2 = "FROM ParkingSlot WHERE checkedIn = :checkedOut";
        Query query2 = session.createQuery(hql2);
        query2.setParameter("checkedOut", checkedOut);
        ParkingSlot parkingSlot = (ParkingSlot)query2.uniqueResult();
        parkingSlot.setCheckedIn(null);
        parkingSlot.setSpotStatus(ParkingSlot.Status.EMPTY);

        checkedOut.setParkingSlot(new ParkingSlot());

        session.delete(checkedOut);
    }

    public void guestCheckOut()
    {
        class_message.payment = calcGuestCredit();
        deleteFromCheckedIn();
        //class_message.current_customer.addToBalance(calcGuestCredit()); //update guests credit
    }
    private boolean noCheckIn()
    {
        String hql = "FROM CheckedIn WHERE personId = :id AND carNumber = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.userId);
        query.setParameter("carId", class_message.carNumber);
        CheckedIn checkedOut = (CheckedIn)query.uniqueResult();
        return checkedOut == null;
    }

    public void customerCheckOut()
    {
        calcCustomerCredit();
        deleteFromCheckedIn();
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case CHECK_ME_OUT_GUEST -> {
                if (noCheckIn()){
                    class_message.response_type = CheckOutMessage.ResponseType.NO_CHECK_IN;
                    break;
                }
                guestCheckOut();
                class_message.response_type = CheckOutMessage.ResponseType.CHECKED_OUT_GUEST;
            }
            case CHECK_ME_OUT -> {
                if (noCheckIn()){
                    class_message.response_type = CheckOutMessage.ResponseType.NO_CHECK_IN;
                    break;
                }
                customerCheckOut();
                class_message.response_type = CheckOutMessage.ResponseType.CHECKED_OUT;
            }
        }
    }

}