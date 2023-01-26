package CarPark.server.handlers;

import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.CheckInMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class CheckInHandler extends MessageHandler {
    private final CheckInMessage class_message;

    public CheckInHandler(CheckInMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (CheckInMessage) this.message;

    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case CHECK_ME_IN_GUEST:
                checkInGuest();
        }
    }

    public void checkInGuest() {
        String parkingLot = class_message.checkedIn.getParkinglot_name();
        CriteriaQuery<ParkingSlot> Query = cb.createQuery(ParkingSlot.class);
        Query.from(ParkingSlot.class);
        List<ParkingSlot> parkingSlots = session.createQuery(Query).getResultList();
        for (ParkingSlot parkingSlot : parkingSlots) {
//        find a parking slot in the chosen parking lot which is also empty
            Parkinglot parkinglot = parkingSlot.getParkinglot();
            if (parkinglot.getId().equals(parkingLot) && parkingSlot.getStatus().equals("EMPTY")) {
                class_message.checkedIn.setParkingSlot(parkingSlot);
                break;
            }
        }
        if (class_message.checkedIn.getParkingSlot() == null)
        {
            class_message.response_type=CheckInMessage.ResponseType.PARKING_LOT_IS_FULL;
            findAlternativeParkingLots(parkingSlots);
        }
        else {
//        store checkedIn guest in the database
            class_message.response_type = CheckInMessage.ResponseType.CHECKED_IN_GUEST;
            session.save(class_message.checkedIn);
            session.flush();
        }
    }

    void findAlternativeParkingLots(List<ParkingSlot> parkingSlots ) {
        for (ParkingSlot parkingSlot : parkingSlots) {
//        find a parking slot in the chosen parking lot which is also empty
            Parkinglot parkinglot = parkingSlot.getParkinglot();
            if (parkingSlot.getStatus().equals("EMPTY")) {
                class_message.setAlternativeParkingLot(parkinglot.getName());
                break;
            }
        }
    }
}
