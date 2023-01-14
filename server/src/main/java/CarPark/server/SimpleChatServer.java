package CarPark.server;

import CarPark.server.password.CipherKey;


public class SimpleChatServer {
    protected static SimpleServer server;


    public static void main(String[] args) throws Exception {
        server = new SimpleServer(3000,new CipherKey());      //builds server
        server.listen();                    //listens to client
    }

}
