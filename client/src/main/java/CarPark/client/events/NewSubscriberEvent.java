package CarPark.client.events;

import CarPark.entities.messages.Message;

public class NewSubscriberEvent {
    private Message message;

    public NewSubscriberEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
