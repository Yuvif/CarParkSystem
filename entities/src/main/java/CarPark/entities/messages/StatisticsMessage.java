package CarPark.entities.messages;

import CarPark.entities.Statistics;

import java.time.LocalDate;

public class StatisticsMessage extends Message{
    public RequestType request_type;
    public ResponseType response_type;
    private int parkinglot_id;
    private Statistics statistics;
    private LocalDate date;

    public StatisticsMessage(MessageType message_type, RequestType request_type, String parkinglot_id, LocalDate date) {
        super(message_type);
        this.request_type = request_type;
        this.parkinglot_id = Integer.parseInt(parkinglot_id);
        this.date = date;
    }

    public enum RequestType {
        GET_STATISTICS
    }

    public enum ResponseType {
        STATISTICS, NO_STATISTICS_AVAILABLE
    }

    public int getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(int parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
