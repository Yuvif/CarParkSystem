package CarPark.client.events;

import CarPark.entities.messages.Message;

public class MessageEvent {
    private Message message;

    public MessageEvent(Message message) {
        this.message = message;
    }

}
