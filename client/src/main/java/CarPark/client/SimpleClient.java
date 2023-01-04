package CarPark.client;

import CarPark.client.controllers.ComplaintHandlerTableController;
import CarPark.client.controllers.Controller;
import CarPark.client.controllers.LoginController;
import CarPark.client.controllers.ParkingListController;
import CarPark.client.events.ErrorEvent;
import CarPark.client.events.MessageEvent;
import CarPark.client.events.NewSubscriberEvent;
import CarPark.client.events.ParkingListMessageEvent;
import CarPark.client.ocsf.AbstractClient;
import CarPark.entities.Complaint;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.ConnectionMessage;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import javafx.collections.FXCollections;
import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;
    //public ParkingLotSkeletonController parkingLotSkeleton;
    private Controller controller;
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

//    public void setController(Controller controller) {
//        this.controller = controller;
//    }

    @Override
    protected void handleMessageFromServer(Object msg) {     //function handles message from server
        Class<?> msgClass = msg.getClass();
        if (Message.class.equals(msgClass)) {
            Message message = (Message) msg;
            EventBus.getDefault().post(new NewSubscriberEvent(message));
        }
        if(ParkingListMessage.class.equals(msgClass))
        {   ParkingListMessage message = (ParkingListMessage) msg;
            EventBus.getDefault().post(new ParkingListMessageEvent(message));
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