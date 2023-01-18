package CarPark.server.handlers;

import CarPark.entities.CheckedIn;
import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.CheckInGuestMessage;
import CarPark.entities.messages.CreateOrderMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.QueryTimeoutException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class CheckInHandler extends MessageHandler {
    private final CheckInGuestMessage class_message;
    public CheckInHandler(CheckInGuestMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (CheckInGuestMessage) this.message;

    }

    @Override
    public void handleMessage() throws Exception {

        String parkingLot = class_message.checkedIn.getParkinglot();

        CriteriaQuery<ParkingSlot> Query = cb.createQuery(ParkingSlot.class);
        Query.from(ParkingSlot.class);
        List<ParkingSlot> parkingSlots = session.createQuery(Query).getResultList();
        List<ParkingSlot> result = new ArrayList<ParkingSlot>();
        for (ParkingSlot parkingSlot : parkingSlots) {
//        find a parkingslot in the chosen parkinglot which is also empty
            Parkinglot parkinglot = parkingSlot.getParkinglot();
            if (parkinglot.getId().equals(parkingLot) && parkingSlot.getStatus().equals("EMPTY")) {
                class_message.checkedIn.setParkingSlot(parkingSlot);
                break;
            }
        }
//        store checkedIn guest in the database
        session.save(class_message.checkedIn);
        session.flush();
        class_message.response_type = CheckInGuestMessage.ResponseType.CHECKED_IN;
    }
}