package CarPark.server.handlers;

import CarPark.entities.messages.StatisticsMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import java.time.LocalDate;

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
                System.out.println("StatisticsHandler: GET_STATISTICS");
                int parkinglot_id = class_message.getParkinglot_id();
                LocalDate date = class_message.getDate();
                getStatisticsOfDayAndParkinglot(parkinglot_id, date);
                break;
        }
    }

    private void getStatisticsOfDayAndParkinglot(int parkinglot_id, LocalDate date) throws Exception {
        var yesterdayStatistics = session.createQuery("from Statistics where parkingLotId = :parkingLotId and date = :date")
                .setParameter("parkingLotId", String.valueOf(parkinglot_id))
                .setParameter("date", date)
                .getResultList();
        if (yesterdayStatistics.size() == 0) {
            class_message.response_type = StatisticsMessage.ResponseType.NO_STATISTICS_AVAILABLE;
        } else {
            //class_message.setStatistics(Statistics)yesterdayStatistics.get(0));
            class_message.response_type = StatisticsMessage.ResponseType.STATISTICS;
        }
    }
}
