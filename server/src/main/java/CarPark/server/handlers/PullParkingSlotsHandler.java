package CarPark.server.handlers;

import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.*;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PullParkingSlotsHandler extends MessageHandler {


    private final PullParkingSlotsMessage class_pslots_message;


    public PullParkingSlotsHandler(PullParkingSlotsMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_pslots_message = (PullParkingSlotsMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {

        switch (class_pslots_message.request_type) {
            case GET_PARKING_SLOTS_REP:
                System.out.println("we got handler- slots");
                class_pslots_message.parkingSlots = getParkingSlots();
                class_pslots_message.response_type = PullParkingSlotsMessage.ResponseType.SET_PARKING_SLOTS_REP;
                class_pslots_message.parkingSlots.removeIf(parkingSlot -> !parkingSlot.getParkinglot().equals(class_pslots_message.parkinglotId)
                        || !(parkingSlot.getStatus().equals(ParkingSlot.Status.RESTRICTED)));

//			complaints.removeIf(complaint -> complaint.getParkinglot().equals(ourParkingLot)
//					|| complaint.getDate().compareTo(fromDate) < 0 || complaint.getDate().compareTo(toDate) > 0);
//		//	complaints.removeIf(complaint -> complaint.getDate().compareTo(fromDate) < 0 || complaint.getDate().compareTo(toDate) > 0);
//
                break;
        }

    }

    private LinkedList<ParkingSlot> getParkingSlots() throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ParkingSlot> customerQuery = builder.createQuery(ParkingSlot.class);
        customerQuery.from(ParkingSlot.class);
        List<ParkingSlot> parkingSlots = session.createQuery(customerQuery).getResultList();
        return new LinkedList<ParkingSlot>(parkingSlots);
    }

    private void generateParkingLots() throws Exception {
        Parkinglot p_l1 = new Parkinglot("Haifa", 4, 36);
        session.save(p_l1);
        session.flush();
        Parkinglot p_l2 = new Parkinglot("Tel Aviv", 7, 63);
        session.save(p_l2);
        session.flush();
    }
}