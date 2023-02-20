package CarPark.server.handlers;

import CarPark.entities.ParkingSlot;
import CarPark.entities.messages.PullParkingSlotsMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PullParkingSlotsHandler extends MessageHandler {


    private final PullParkingSlotsMessage class_message;


    public PullParkingSlotsHandler(PullParkingSlotsMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (PullParkingSlotsMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {

        switch (class_message.request_type) {
            case GET_PARKING_SLOTS_REP:
                class_message.parkingSlots = getParkingSlots();
                class_message.response_type = PullParkingSlotsMessage.ResponseType.SET_PARKING_SLOTS_REP;

//			complaints.removeIf(complaint -> complaint.getParkinglot().equals(ourParkingLot)
//					|| complaint.getDate().compareTo(fromDate) < 0 || complaint.getDate().compareTo(toDate) > 0);
//		//	complaints.removeIf(complaint -> complaint.getDate().compareTo(fromDate) < 0 || complaint.getDate().compareTo(toDate) > 0);
//
                break;
        }

    }

    private LinkedList<ParkingSlot> getParkingSlots() throws IOException {
        CriteriaQuery<ParkingSlot> query = cb.createQuery(ParkingSlot.class);
        query.from(ParkingSlot.class);
        List<ParkingSlot> p_s = session.createQuery(query).getResultList();
        LinkedList<ParkingSlot> parkingSlots = new LinkedList<>(p_s);
        return parkingSlots;
    }

}