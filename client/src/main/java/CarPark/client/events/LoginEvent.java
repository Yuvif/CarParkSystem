package CarPark.client.events;

import CarPark.entities.messages.LoginMessage;

public class LoginEvent extends MessageEvent {
    public LoginEvent(LoginMessage message) {
        super(message);
    }
}
