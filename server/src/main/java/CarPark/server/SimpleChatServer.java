package CarPark.server;


public class SimpleChatServer {
    protected static SimpleServer server;


    public static void main(String[] args) throws Exception {
        server = new SimpleServer(3000);      //builds server
        server.listen();                    //listens to client
    }

}
