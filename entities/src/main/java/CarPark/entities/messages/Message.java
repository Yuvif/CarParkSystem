package CarPark.entities.messages;

import java.io.Serializable;


public class Message implements Serializable {
    public MessageType message_type;

    public Message(MessageType message_type) {
        this.message_type = message_type;
    }


    public enum MessageType {
        REQUEST,
        RESPONSE
    }


}
