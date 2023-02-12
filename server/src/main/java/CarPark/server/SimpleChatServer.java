package CarPark.server;


import java.io.IOException;

public class SimpleChatServer {
    protected static SimpleServer server;



    public static void main(String[] args) throws Exception {
        server = new SimpleServer(3100);      //builds server
        server.listen();                    //listens to client
    }

}
