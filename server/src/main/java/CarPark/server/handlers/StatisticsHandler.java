package CarPark.server.handlers;

import CarPark.entities.messages.Message;
import CarPark.entities.messages.StatisticsMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Date;

public class StatisticsHandler extends MessageHandler{

    private final StatisticsMessage class_message;

    public StatisticsHandler(StatisticsMessage msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (StatisticsMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case GET_STATISTICS:
                int parkinglot_id = class_message.getParkinglot_id();
                LocalDate date = class_message.getDate();
                getStatisticsOfDayAndParkinglot(parkinglot_id, date);
                class_message.response_type = StatisticsMessage.ResponseType.STATISTICS;
                break;
        }

    }

    private void getStatisticsOfDayAndParkinglot(int parkinglot_id, LocalDate date) throws Exception
    {
//        check if there is an entry in the table "Statistics" for that date and parkinglot
//        if there is, return the statistics
//        if there isn't, return null
//          prepare a query to check if there is an entry in the table "Statistics" for that date and parkinglot
//          if there is, return the statistics
//          if there isn't, return null



    }
}
