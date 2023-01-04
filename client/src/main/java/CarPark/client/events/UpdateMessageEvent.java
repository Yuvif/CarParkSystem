package CarPark.client.events;

import CarPark.entities.messages.Message;

public class UpdateMessageEvent {
    private Message message;

    public UpdateMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
