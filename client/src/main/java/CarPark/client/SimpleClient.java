
package CarPark.client;

import CarPark.client.ocsf.AbstractClient;
import CarPark.entities.User;
import CarPark.entities.messages.*;
import org.greenrobot.eventbus.EventBus;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;
    private static User current_user;
    public SimpleClient(String host, int port) {
        super(host, port);
    }

    public static void setCurrent_user(User user)
    {
        current_user = user;
    }

    public static User getCurrent_user()
    {
        return current_user;
    }

    public static void main(String[] args) {
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3100);
        }
        return client;
    }


    @Override
    protected void handleMessageFromServer(Object msg) {     //function handles message from server
        Class<?> msgClass = msg.getClass();
        if (ParkingListMessage.class.equals(msgClass)) {
            ParkingListMessage message = (ParkingListMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (PricesMessage.class.equals(msgClass)) {
            PricesMessage message = (PricesMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (OrderMessage.class.equals(msgClass)) {
            OrderMessage message = (OrderMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (LoginMessage.class.equals(msgClass)){
            LoginMessage message = (LoginMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (RegisterUserMessage.class.equals(msgClass)){
            RegisterUserMessage message = (RegisterUserMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (MembershipMessage.class.equals(msgClass)) {
            MembershipMessage message = (MembershipMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (ComplaintMessage.class.equals(msgClass)){
            ComplaintMessage message = (ComplaintMessage) msg;
            EventBus.getDefault().post(message);
        }
        if(ParkingLotMapMessage.class.equals(msgClass)) {
            ParkingLotMapMessage message = (ParkingLotMapMessage) msg;
            EventBus.getDefault().post(message);
        }
        if (StatisticsMessage.class.equals(msgClass)) {
            StatisticsMessage message = (StatisticsMessage) msg;
            EventBus.getDefault().post(message);
        }
        if(CheckInMessage.class.equals(msgClass)){
            CheckInMessage message = (CheckInMessage) msg;
            EventBus.getDefault().post(message);
        }
        if(CheckOutMessage.class.equals(msgClass)){
            CheckOutMessage message = (CheckOutMessage) msg;
            EventBus.getDefault().post(message);
        }
        if(PullParkingSlotsMessage.class.equals(msgClass)){
            PullParkingSlotsMessage message = (PullParkingSlotsMessage) msg;
            EventBus.getDefault().post(message);
        }
    }
}

