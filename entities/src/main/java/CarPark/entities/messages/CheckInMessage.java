package CarPark.entities.messages;

import CarPark.entities.CheckedIn;

public class CheckInMessage extends Message {
    public RequestType request_type;
    public ResponseType response_type;
    public CheckedIn checkedIn;

    public enum RequestType {
        CHECK_ME_IN,
        CHECK_ME_IN_GUEST
    }

    public enum ResponseType {
        CHECKED_IN,
        CHECKED_IN_GUEST
    }

    public CheckInMessage(MessageType message_type, CheckInMessage.RequestType request_type, CheckedIn checkedIn) {
        super(message_type);
        this.request_type = request_type;
        this.checkedIn = checkedIn;
    }
}
