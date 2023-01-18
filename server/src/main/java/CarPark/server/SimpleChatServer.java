package CarPark.server;

import java.io.IOException;


public class SimpleChatServer {
    protected static SimpleServer server;


    public static void main(String[] args) throws IOException {
        server = new SimpleServer(3000);      //builds server
        server.listen();                    //listens to client
    }

}
