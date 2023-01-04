package CarPark.server;


import CarPark.entities.*;
import CarPark.entities.messages.ConnectionMessage;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.ParkingListMessage;
import CarPark.server.handlers.LoginHandler;
import CarPark.server.handlers.MessageHandler;
import CarPark.server.handlers.ParkingListHandler;
import CarPark.server.ocsf.AbstractServer;
import CarPark.server.ocsf.ConnectionToClient;
import CarPark.server.ocsf.SubscribedClient;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    public static Session session;// encapsulation make public function so this can be private

    public SimpleServer(int port) {
        super(port);
    }


//	private static <T> void addNewInstance(T obj) {
//
//		SimpleChatServer.session.beginTransaction();
//		SimpleChatServer.session.save(obj);
//		SimpleChatServer.session.flush();
//		SimpleChatServer.session.getTransaction().commit();
//	}

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.

        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(Parkinglot.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }

//
//	ArrayList<Parkinglot> p_l = new ArrayList<>();
//	ArrayList<Price> p_t = new ArrayList<>();
//	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    public static void main(String[] args) throws IOException {


        if (args.length != 1) {
            System.out.println("Required argument: <port>");
        } else {
            SimpleServer server = new SimpleServer(Integer.parseInt(args[0]));
            server.listen();
        }
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {
        session = getSessionFactory().openSession();
        session.beginTransaction();
        MessageHandler handler = null;
        Class<?> msgClass = msg.getClass();
        if (ConnectionMessage.class.equals(msgClass))
        {
            SubscribedClient connection = new SubscribedClient(client);
            SubscribersList.add(connection);
            Message message = new Message("client added successfully");
            client.sendToClient(message);
        }
        if (LoginMessage.class.equals(msgClass)) {
            handler = new LoginHandler((LoginMessage) msg, session, client);
        }
        if (ParkingListMessage.class.equals(msgClass))
        {
            handler = new ParkingListHandler((ParkingListMessage) msg, session, client);
        }
        if(handler!=null) {
            handler.handleMessage();
            handler.message.message_type = Message.MessageType.RESPONSE;
            client.sendToClient(handler.message);
        }
    }
/*
	private void addComplaint(LinkedList<Object> msg) {
		addNewInstance((Complaint) msg.get(1));
	}

	private void pullOpenComplaints(ConnectionToClient client) throws IOException {
		List<Complaint> complaints = SimpleChatServer.getAllOpenComplaints();
		List<Object> msg = new LinkedList<>();
		msg.add("#PULL_COMPLAINTS");
		msg.add(complaints);
		client.sendToClient(msg);
	}

	private void pullParkingLots(ConnectionToClient client) throws IOException {
		List<Parkinglot> parkinglots = SimpleChatServer.getAllParkingLots();
		List<Object> msg = new LinkedList<>();
		msg.add("#PULL_PARKINGLOTS");
		msg.add(parkinglots);
		client.sendToClient(msg);

	*/


}
