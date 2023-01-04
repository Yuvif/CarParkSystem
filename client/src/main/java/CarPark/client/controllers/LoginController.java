package CarPark.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void login(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {

    }
}


