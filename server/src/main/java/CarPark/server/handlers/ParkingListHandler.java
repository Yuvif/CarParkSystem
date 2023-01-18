package CarPark.server.handlers;

import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import CarPark.entities.messages.ParkingSlotsMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ParkingListHandler extends MessageHandler {


    private final ParkingListMessage class_message;

    public ParkingListHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (ParkingListMessage) this.message;
        class_message.response_type = ParkingListMessage.ResponseType.SET_ALL_PARKING_LOTS;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_ALL_PARKING_LOTS:
                class_message.parkingList = getParkingLots();
                break;
        }
    }

    private List<Parkinglot> getParkingLots() throws Exception {
        generateParkingLots();
        CriteriaQuery<Parkinglot> query = cb.createQuery(Parkinglot.class);
        query.from(Parkinglot.class);
        List<Parkinglot> data = session.createQuery(query).getResultList();
        return data;
    }

    private void generateParkingLots() throws Exception {
        Parkinglot p_l1 = new Parkinglot("Haifa", 4, 36);
        session.save(p_l1);
        session.flush();
        Parkinglot p_l2 = new Parkinglot("Tel Aviv", 7, 63);
        session.save(p_l2);
        session.flush();
        Parkinglot p_l3 = new Parkinglot("Jerusalem", 8, 72);
        session.save(p_l3);
        session.flush();
        Parkinglot p_l4 = new Parkinglot("Be'er Sheva", 6, 54);
        session.save(p_l4);
        session.flush();
        Parkinglot p_l5 = new Parkinglot("Eilat", 5, 45);
        session.save(p_l5);
        session.flush();
    }
}