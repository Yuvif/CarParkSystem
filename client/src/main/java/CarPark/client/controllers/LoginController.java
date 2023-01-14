package CarPark.client.controllers;

import CarPark.client.SimpleChatClient;
import CarPark.client.SimpleClient;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private Button check_in;

    @FXML
    private Button check_out;

    @FXML
    private Label wrongLogin;

    @FXML
    private Button loginBtn;

    @FXML
    private Button order;

    @FXML
    private PasswordField password;

    @FXML
    private Button register;

    @FXML
    private TextField userID;


    @FXML
    void initialize() throws IOException
    {
        EventBus.getDefault().register(this);
    }

    @FXML
    void makeOrder(ActionEvent event) throws IOException
    {
        SimpleChatClient.setRoot("CreateOrder");
    }

    @FXML
    void register(ActionEvent event) throws IOException
    {
        SimpleChatClient.setRoot("RegisterAsMember");
    }

    @FXML
    void checkIn(ActionEvent event) throws IOException
    {
        //SimpleChatClient.setRoot("RegisterAsMember");
    }

    @FXML
    void checkOut(ActionEvent event) throws IOException
    {
        //SimpleChatClient.setRoot("RegisterAsMember");
    }

    @FXML
    void login(ActionEvent event) throws IOException, NoSuchAlgorithmException
    {
        if (checkIdValidity(userID.getText()) && checkIdValidity(password.getText())) //check if password and username are valid
        {
            //if valid send request to login with secured password
            long userId = Long.parseLong(userID.getText());
            String userPass = SecurePassword(password.getText(), getSalt());
            LoginMessage msg =
                    new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN, userId, userPass);
            SimpleClient.getClient().sendToServer(msg);
        }
        else
            loginFailed();
    }


    public static String SecurePassword(String password, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private boolean checkIdValidity(String id)
    {
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    private boolean CheckPassValidity(String pass)
    {
        String regex = ".{7,}$";    //password needs to be at least 7 chars
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    @Subscribe
    public void newResponse(LoginMessage new_message) throws IOException
    {
        switch (new_message.response_type) {
            case LOGIN_FAILED:
                loginFailed();
            case LOGIN_SUCCEED_CUSTOMER:
                SimpleChatClient.setRoot("Customer");
            case LOGIN_SUCCEED_EMPLOYEE:
                SimpleChatClient.setRoot("Employee");
        }
    }

    public void loginFailed()
    {
        wrongLogin.setText("Invalid Username Or Password!");
    }

}


