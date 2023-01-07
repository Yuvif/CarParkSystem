package CarPark.client;

import CarPark.client.controllers.ComplaintHandlerTableController;
import CarPark.client.events.MessageEvent;
import CarPark.client.events.ParkingListEvent;
import CarPark.client.ocsf.AbstractClient;
import CarPark.entities.messages.ParkingListMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;


    public SimpleClient(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }


    @Override
    protected void handleMessageFromServer(Object msg) {     //function handles message from server
        Class<?> msgClass = msg.getClass();
        if(ParkingListMessage.class.equals(msgClass))
        {
            ParkingListMessage message = (ParkingListMessage) msg;
            EventBus.getDefault().post(new ParkingListEvent(message.parkingList));
        }
    }


//    private void pushComplaints(LinkedList<Object> msg) {
//        ComplaintHandlerTableController tableController = (ComplaintHandlerTableController) controller;
//        tableController.pullComplaints(FXCollections.observableArrayList(((List<Complaint>) msg.get(1))));
//    }
//
//    private void pushParkinglots(LinkedList<Object> msg) {
//        ParkingListController tableController = (ParkingListController) controller;
//        tableController.pullParkinglots(FXCollections.observableArrayList(((List<Parkinglot>) msg.get(1))));
//    }

}