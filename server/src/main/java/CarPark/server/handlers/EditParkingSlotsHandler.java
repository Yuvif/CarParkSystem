package CarPark.server.handlers;

import CarPark.entities.*;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import CarPark.entities.messages.ParkingSlotsMessage;
import CarPark.entities.messages.PricesMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.QueryTimeoutException;
import org.hibernate.Session;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EditParkingSlotsHandler extends MessageHandler {

    private ParkingSlotsMessage class_message;

    public EditParkingSlotsHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (ParkingSlotsMessage) this.message;
    }

    public void setClass_message(Message msg) {
        this.class_message = (ParkingSlotsMessage) msg;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_ALL_PARKING_LOTS:
                System.out.println("we got handler- List");
                class_message.parkingList = getParkingList();
                class_message.response_type = ParkingSlotsMessage.ResponseType.SET_ALL_PARKING_LOTS;
                break;
            case GET_SELECTED_PARKING_SLOTS:
                System.out.println("we got handler- slots");
                class_message.parkingSlots = getParkingSlots(class_message.parkinglotId);
                class_message.response_type = ParkingSlotsMessage.ResponseType.SET_PARKING_SLOTS;
                break;
            case SET_PARKING_SLOT_STATUS:
                //editPrice();
                class_message.response_type = ParkingSlotsMessage.ResponseType.PARKING_SLOT_EDITED;
                break;
        }
    }

    private List<Parkinglot> getParkingList() throws Exception {
        CriteriaQuery<Parkinglot> query = cb.createQuery(Parkinglot.class);
        query.from(Parkinglot.class);
        List<Parkinglot> data = session.createQuery(query).getResultList();
        return data;
    }

    private List<ParkingSlot> getParkingSlots(String parkinglot2) throws IOException {
        CriteriaQuery<ParkingSlot> Query = cb.createQuery(ParkingSlot.class);
        Query.from(ParkingSlot.class);
        List<ParkingSlot> parkingSlots = session.createQuery(Query).getResultList();
        List<ParkingSlot> result= new ArrayList<ParkingSlot>();
        for(ParkingSlot slot: parkingSlots)
        {
            if (slot.getParkinglot().getId().equals(parkinglot2)) {
                result.add(slot);
            }
        }
        return result;
    }
    private List<Parkinglot> generateParkinglots() throws Exception {       //generates new Parkinglots
        List<Parkinglot> parkinglots = new LinkedList<Parkinglot>();
        String[] plNames = new String[]{"CPS Haifa", "CPS Tel-Aviv", "CPS Be'er Sheva", "CPS Rehovot", "CPS Jerusalem", "CPS Eilat"};
        int[] plParksPerRow = new int[]{5,4,6,8,5,6};
        int[] totalParkingSpots = new int[]{45,36,54,72,45,54};
        ParkingSlot[] parkingSlots = new ParkingSlot[6];

        for (int i = 0; i < plNames.length; i++) {

            Parkinglot parkinglot = new Parkinglot(plNames[i], plParksPerRow[i],totalParkingSpots[i]);

            for(int j = 0 ; j<6 ; j++)
            {
                parkingSlots[j] = new ParkingSlot(parkinglot);
                parkinglot.addParkingSlots(parkingSlots[j]);
                session.save(parkingSlots[j]);
            }
            parkinglot.setTotalParkingLots(3*3*parkinglot.getParksPerRow());
            parkinglots.add(parkinglot);

            session.save(parkinglot);   //saves and flushes to database
            session.flush();
//            Complaint complaint = new Complaint(new Date(), "fucku",parkinglot);
//            session.save(complaint);
//            session.flush();
        }
//        CheckedIn checkedIn = new CheckedIn(new Date(), 1234,1234,"test",new Date(),parkingSlots[2]);
//        session.save(checkedIn);
//        session.flush();

        return parkinglots;
    }

//    private void editParkingSlotStatus() throws Exception
//    {
//        Price old_price = session.get(Price.class, class_message.new_price.getId());
//        old_price.setPrice(class_message.new_price.getPrice());
//    }
}