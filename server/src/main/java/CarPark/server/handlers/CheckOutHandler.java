package CarPark.server.handlers;

import CarPark.entities.CheckedIn;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Price;
import CarPark.entities.messages.CheckOutMessage;
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
        String hql = "FROM CheckedIn WHERE personId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        return (CheckedIn)query.uniqueResult();
    }

    public CheckedIn findCheckedInCustomer()
    {
        String hql = "FROM CheckedIn WHERE personId = :id AND carNumber = :carId";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        query.setParameter("carId", class_message.carNumber);
        return (CheckedIn)query.uniqueResult();
    }



    public double calculateCharges(LocalDateTime arrivalTime, LocalDateTime leavingTime, LocalDateTime estimatedLeavingTime, double pricePH)
    {
        if(leavingTime.isBefore(estimatedLeavingTime) ||  leavingTime.isEqual(estimatedLeavingTime))
        {
            double differenceInMinutes = ChronoUnit.MINUTES.between(arrivalTime, leavingTime);
            double differenceInHours = differenceInMinutes / 60;
            return pricePH * differenceInHours;
        }
        else
        {
            double differenceInMinutes = ChronoUnit.MINUTES.between(arrivalTime, estimatedLeavingTime);
            double differenceInHours = differenceInMinutes / 60;
            double differenceInMinutes_TimeDeviation = ChronoUnit.MINUTES.between(estimatedLeavingTime, leavingTime);
            double differenceInHours_TimeDeviation = differenceInMinutes_TimeDeviation / 60;
            return  pricePH * differenceInHours + 1.1 * pricePH * differenceInHours_TimeDeviation;
        }
    }

    //calculation of charges for guests
    public double calcGuestBalance()
    {
        String hql = "FROM Price WHERE parkingType = :type";
        Query query = session.createQuery(hql);
        query.setParameter("type", "Casual parking");
        Price price = (Price) query.uniqueResult();
        double pricePH = price.getPrice();

        CheckedIn checkedInGuest = findCheckedInGuest();
        return calculateCharges(checkedInGuest.getEntryDate() ,checkedInGuest.getExitEstimatedDate(),LocalDateTime.now(), pricePH);
    }

    public void guestCheckOut()
    {
        CheckedIn checkedInGuest = findCheckedInGuest();
        checkedInGuest.getParkingSlot().setSpotStatus(ParkingSlot.Status.EMPTY);

        //delete the checkedIn from the session
    }

    public void customerCheckOut()
    {
        CheckedIn checkedInCustomer = findCheckedInCustomer();
        checkedInCustomer.getParkingSlot().setSpotStatus(ParkingSlot.Status.EMPTY);

        //delete the checkedIn from the session
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case CHECK_ME_OUT_GUEST -> {
                guestCheckOut();
                class_message.response_type = CheckOutMessage.ResponseType.CHECKED_OUT_GUEST;
            }
            case CHECK_ME_OUT -> {
                customerCheckOut();
                class_message.response_type = CheckOutMessage.ResponseType.CHECKED_OUT;
            }
        }
    }


}
