package CarPark.client.controllers;

import CarPark.client.SimpleClient;
import CarPark.entities.Customer;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.RegisterUserMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static CarPark.client.controllers.Controller.sendAlert;

public class RegisterUserController {
    @FXML
    private TextField userID;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
    }


    @FXML
    void signUp(ActionEvent event) throws Exception
    {
        if (checkValidity()) // create membership entity and send it to the server
        {
            Customer new_customer = createCustomer();
            RegisterUserMessage msg = new RegisterUserMessage(Message.MessageType.REQUEST, RegisterUserMessage.RequestType.REGISTER, new_customer);
            try {
                SimpleClient.getClient().sendToServer(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Customer createCustomer() throws Exception {
            Customer new_customer = new Customer(Long.parseLong(userID.getText()),firstName.getText(),lastName.getText(),
                    email.getText(),0,password.getText());
            return  new_customer;
    }

    private boolean checkValidity() {
        if (checkIdValidity(userID.getText()) && checkPassValidity(password.getText()))
            return true;
        return false;
    }


    private boolean checkIdValidity(String id) {
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    private boolean checkPassValidity(String pass)
    {
        String regex = ".{7,}$";    //password needs to be at least 7 chars
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    @Subscribe
    public void new_response(RegisterUserMessage new_message)
    {
        switch (new_message.response_type){
            case REGISTRATION_SUCCEEDED:
                sendAlert("Registration succeed, welcome:" + new_message.newCustomer.getFirstName(),
                        "New User", Alert.AlertType.INFORMATION);
        }
    }

}
