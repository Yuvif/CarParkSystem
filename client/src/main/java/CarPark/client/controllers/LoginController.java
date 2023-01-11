package CarPark.client.controllers;

import CarPark.client.SimpleClient;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private Button button;
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


    public void login(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        if (checkIdValidity(userID.getText()) && checkIdValidity(password.getText())) //check if password and username are valid
        {
            //if valid send request to login with secured password
            long userId = Long.parseLong(userID.getText());
            String userPass = SecurePassword(password.getText(), getSalt());
            LoginMessage msg =
                    new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGIN,userId,userPass);
            SimpleClient.getClient().sendToServer(msg);
        }
        else
            wrongLogin.setText("Invalid Username Or Password!");
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

    private boolean checkIdValidity(String id) {
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


}


