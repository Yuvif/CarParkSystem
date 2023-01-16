package CarPark.server;


import CarPark.entities.*;
import CarPark.entities.messages.*;
import CarPark.server.handlers.*;
import CarPark.server.ocsf.AbstractServer;
import CarPark.server.ocsf.ConnectionToClient;
import CarPark.server.ocsf.SubscribedClient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    public static Session session;// encapsulation make public function so this can be private

    public SimpleServer(int port) {
        super(port);
    }


    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Parkinglot.class);
        configuration.addAnnotatedClass(Price.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(ParkingSlot.class);
        configuration.addAnnotatedClass(CheckedIn.class);
        configuration.addAnnotatedClass(Complaint.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {
        try {
            MessageHandler handler = null;
            Class<?> msgClass = msg.getClass();
            if (ConnectionMessage.class.equals(msgClass)) { //New client connection
                SubscribedClient connection = new SubscribedClient(client);
                SubscribersList.add(connection);
                session = getSessionFactory().openSession();// Create new session for connection
            } else { //Get client requests
                session.beginTransaction();
                if (LoginMessage.class.equals(msgClass)) {
                    handler = new LoginHandler((LoginMessage) msg, session, client);
                } else if (ParkingListMessage.class.equals(msgClass)) {
                    handler = new ParkingListHandler((ParkingListMessage) msg, session, client);
                } else if (PricesMessage.class.equals(msgClass)) {
                    handler = new PricesTableHandler((PricesMessage) msg, session, client);
                } else if (CreateOrderMessage.class.equals(msgClass)) {
                    handler = new OrderHandler((CreateOrderMessage) msg, session, client);
                } else if (ParkingSlotsMessage.class.equals(msgClass)) {
                    handler = new EditParkingSlotsHandler((ParkingSlotsMessage) msg, session, client);
                }else if (PullParkingSlotsMessage.class.equals(msgClass)) {
                    handler = new PullParkingSlotsHandler((PullParkingSlotsMessage) msg, session, client);
                }else if (PullOrdersMessage.class.equals(msgClass)) {
                    handler = new PullOrdersHandler((PullOrdersMessage) msg, session, client);
                }else if (ComplaintMessage.class.equals(msgClass)) {
                    handler = new ComplaintHandler((ComplaintMessage) msg, session, client);
                    System.out.println("we got here");
                }
                if (handler != null) {
                    handler.handleMessage();
                    session.getTransaction().commit();
                    handler.message.message_type = Message.MessageType.RESPONSE;
                    client.sendToClient(handler.message);
                }
            }
        } catch (Exception exception) {
            if (session != null)
                session.getTransaction().rollback();
            exception.printStackTrace();
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
