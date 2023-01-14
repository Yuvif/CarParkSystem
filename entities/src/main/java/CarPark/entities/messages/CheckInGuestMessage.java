package CarPark.entities.messages;

import CarPark.entities.CheckedIn;

import java.util.List;

public class CheckInGuestMessage extends Message {
    public CheckInGuestMessage.RequestType request_type;
    public CheckInGuestMessage.ResponseType response_type;
    public CheckedIn checkedIn;

    public enum RequestType {
        CHECK_ME_IN
    }

    public enum ResponseType {
        CHECKED_IN
    }

    public CheckInGuestMessage(MessageType message_type, CheckInGuestMessage.RequestType request_type, CheckedIn checkedIn) {
        super(message_type);
        this.request_type = request_type;
        this.checkedIn = checkedIn;
    }
}
