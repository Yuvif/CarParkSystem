package CarPark.client.events;

import CarPark.entities.messages.Message;

public class ErrorEvent {
    private Message message;

    public ErrorEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
