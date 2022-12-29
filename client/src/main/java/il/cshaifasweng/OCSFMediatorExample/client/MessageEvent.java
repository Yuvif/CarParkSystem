package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class MessageEvent  {
    private Message message;

    public Message getMessage() {
        return message;
    }
    public MessageEvent(Message message) {
        this.message = message;
    }

}
