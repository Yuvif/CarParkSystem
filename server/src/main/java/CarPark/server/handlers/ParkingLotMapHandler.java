package CarPark.server.handlers;

import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingLotMapMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class ParkingLotMapHandler extends MessageHandler {
    private final ParkingLotMapMessage class_message;

    public ParkingLotMapHandler(Message msg, Session session, ConnectionToClient client)
    {
        super(msg, session, client);
        this.class_message = (ParkingLotMapMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case SHOW_PARKING_LOT_MAP:
                //algorithm-*********************
                class_message.response_type = ParkingLotMapMessage.ResponseType.SEND_PARKING_LOT_MAP;
                break;
            case ARRANGE_MAP:
                //update the map
                class_message.response_type = ParkingLotMapMessage.ResponseType.SEND_ARRANGED_MAP;
                break;
        }
    }
}
