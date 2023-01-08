package CarPark.server;


import CarPark.entities.*;
import CarPark.entities.messages.*;
import CarPark.server.handlers.LoginHandler;
import CarPark.server.handlers.MessageHandler;
import CarPark.server.handlers.ParkingListHandler;
import CarPark.server.handlers.PricesTableHandler;
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
import java.util.logging.Level;


public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    public static Session session;// encapsulation make public function so this can be private

    public SimpleServer(int port) {
        super(port);
    }



    private static SessionFactory getSessionFactory() throws HibernateException {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Parkinglot.class);
        configuration.addAnnotatedClass(Price.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {
        try {

            MessageHandler handler = null;
            Class<?> msgClass = msg.getClass();
            if (ConnectionMessage.class.equals(msgClass)) {
                SubscribedClient connection = new SubscribedClient(client);
                SubscribersList.add(connection);
            }
            else
            {
                session = getSessionFactory().openSession();
                session.beginTransaction();
            }

            if (LoginMessage.class.equals(msgClass)) {
                handler = new LoginHandler((LoginMessage) msg, session, client);
            }
            else if (ParkingListMessage.class.equals(msgClass)) {
                handler = new ParkingListHandler((ParkingListMessage) msg, session, client);
            }
            else if (PricesMessage.class.equals(msgClass)) {
                handler = new PricesTableHandler((PricesMessage) msg, session, client);
            }
            if (handler != null) {
                handler.handleMessage();
                session.getTransaction().commit();
                handler.message.message_type = Message.MessageType.RESPONSE;
                client.sendToClient(handler.message);
            }
        } catch (Exception exception) {
            if(session!=null)
                session.getTransaction().rollback();
            exception.printStackTrace();
        } finally {
            session.close();
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
