package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController{

    public Button register;
    public Button check_in;
    public Button check_out;
    public Button order;


    @FXML
    private TextField userID;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongLogin;


    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
    }


    public void login(ActionEvent event) throws Exception {
        if (checkIdValidity(userID.getText()) && checkPassValidity(password.getText())) //check if password and username are valid
        {
            //if valid send request to login with secured password
            long userId = Long.parseLong(userID.getText());
            String userPass = password.getText();
            LoginMessage msg =
                    new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN,userId,userPass);
            SimpleClient.getClient().sendToServer(msg);
        }
        else
            setWrongLogin();
    }

    @FXML
    public void setWrongLogin() throws IOException {
        wrongLogin.setText("Invalid Username Or Password!");
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
    public void newResponse(LoginMessage new_message) throws IOException {
        switch (new_message.response_type) {
            case LOGIN_SUCCEED_CUSTOMER -> {
                SimpleClient.setCurrent_user(new_message.getUser());
                SimpleChatClient.setRoot("CustomerPage");}
            case LOGIN_SUCCEED_EMPLOYEE -> {
                SimpleClient.setCurrent_user(new_message.getUser());
                SimpleChatClient.setRoot("EmployeePage");
            }
            case LOGIN_FAILED -> Platform.runLater(() -> {
                try {
                    setWrongLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            case ALREADY_LOGGED -> Platform.runLater(() -> {
                alreadyLogIn();
            });
        }
    }

    //Need to check if already registered to system!!
    @FXML
    public void alreadyLogIn()
    {
        wrongLogin.setText("This user is already logged in to system");
    }


    public void signUp(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("RegisterUser");
    }

    public void checkInAsGuest(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("CheckInGuest");
    }

    public void checkOutAsGuest(ActionEvent event) {
    }

    public void prices(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("Prices");
    }
}
