package CarPark.client;

import CarPark.client.events.MessageEvent;
import CarPark.entities.messages.ConnectionMessage;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {
    public static SimpleClient client;
    private static Scene scene;

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
//        send a message to the server to check if the connection is established

        ConnectionMessage message = new ConnectionMessage(Message.MessageType.REQUEST);
        client.sendToServer(message);
        System.out.println("Client started");
//        scene = new Scene(loadFXML("StatisticsView"), 640, 480);
        scene = new Scene(loadFXML("Login"), 640, 480);
      //  scene = new Scene(loadFXML("ComplaintInspectionTable"), 640, 520);
        // = new Scene(loadFXML("MenuEmployee"), 640, 520);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        if (SimpleClient.getCurrent_user()!=null) {
            LoginMessage logout = new LoginMessage(Message.MessageType.REQUEST, LoginMessage.RequestType.LOGOUT_BY_TERMINATION, String.valueOf(SimpleClient.getCurrent_user().getId()));
            SimpleClient.getClient().sendToServer(logout);
        }
        try {
            client.closeConnection();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(MessageEvent message) {
    }

}