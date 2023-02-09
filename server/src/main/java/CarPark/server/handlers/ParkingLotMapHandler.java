package CarPark.server.handlers;

import CarPark.entities.ParkingSlot;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingLotMapMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class ParkingLotMapHandler extends MessageHandler {
    private final ParkingLotMapMessage class_message;

    public ParkingLotMapHandler(Message msg, Session session, ConnectionToClient client)
    {
        super(msg, session, client);
        this.class_message = (ParkingLotMapMessage) this.message;
    }

    public int getParkingLotRows()
    {
        String hql = "FROM Parkinglot WHERE name = :pl_name";
        Query query = session.createQuery(hql);
        query.setParameter("pl_name", class_message.pl_name);
        Parkinglot parkinglot = (Parkinglot)query.uniqueResult();
        return parkinglot.getParksPerRow();
    }

    public List<ParkingSlot> getParkingSlots()
    {
        CriteriaQuery<ParkingSlot> query = cb.createQuery(ParkingSlot.class);
        query.from(ParkingSlot.class);
        List<ParkingSlot> data = session.createQuery(query).getResultList();
        return data;
    }

    public void updateParkingSlotsTable(List<ParkingSlot> parkingSlotList)
    {
        for(ParkingSlot parkingSlot : parkingSlotList)
        {
            ParkingSlot currParkingSlot = session.get(ParkingSlot.class, parkingSlot.getSerialId());
            currParkingSlot.setSpotStatus(parkingSlot.getSpotStatus());
            session.update(currParkingSlot);
        }
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_ROW:
                class_message.rows = getParkingLotRows();
                class_message.response_type = ParkingLotMapMessage.ResponseType.SET_ROW;
                break;
            case GET_PARKING_SLOTS:
                class_message.parkingSlots = getParkingSlots();
                class_message.rows = getParkingLotRows();
                class_message.response_type = ParkingLotMapMessage.ResponseType.SEND_PARKING_SLOTS;
                break;
            case SHOW_PARKING_LOT_MAP:
                updateParkingSlotsTable(class_message.parkingSlots);
                class_message.response_type = ParkingLotMapMessage.ResponseType.SEND_PARKING_LOT_MAP;
                break;
        }
    }
}
