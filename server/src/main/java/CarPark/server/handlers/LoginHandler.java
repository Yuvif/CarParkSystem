package CarPark.server.handlers;

import CarPark.entities.messages.LoginMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

public class LoginHandler extends MessageHandler {
    public LoginMessage class_message;

    public LoginHandler(LoginMessage message, Session session, ConnectionToClient client) {
        super(message, session, client);
        this.class_message = (LoginMessage) this.message;
    }

    @Override
    public void handleMessage() {
        System.out.println(class_message.getPassword());
        System.out.println(class_message.getUserName());
    }
}
