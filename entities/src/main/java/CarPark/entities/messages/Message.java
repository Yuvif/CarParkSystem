package CarPark.entities.messages;

import java.io.Serializable;


public class Message implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public enum MessageType {
        REQUEST,
        RESPONSE
    }
    public MessageType message_type;

    public Message(MessageType message_type) {
        this.message_type = message_type;
    }
    public Message(String msg){
        message = msg;
    }

}
