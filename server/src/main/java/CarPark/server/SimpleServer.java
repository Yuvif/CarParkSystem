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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
        configuration.addAnnotatedClass(CheckedIn.class);
        configuration.addAnnotatedClass(ParkingSlot.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException, SQLException {
        try {
            MessageHandler handler = null;
            Class<?> msgClass = msg.getClass();
            System.out.println("Message received: " + msgClass.getName() + " from " + client);
            if (ConnectionMessage.class.equals(msgClass)) { //New client connection
                SubscribedClient connection = new SubscribedClient(client);
                SubscribersList.add(connection);
                System.out.println("New client connection");
                session = getSessionFactory().openSession();// Create new session for connection
                session.beginTransaction();
                System.out.println("New client connected");
                List<Parkinglot> parkinglots = new LinkedList<Parkinglot>();
                String[] plNames = new String[]{"CPS Haifa", "CPS Tel-Aviv", "CPS Be'er Sheva", "CPS Rehovot", "CPS Jerusalem", "CPS Eilat"};
                int[] plParksPerRow = new int[]{5,4,6,8,5,6};
                int[] totalParkingSpots = new int[]{45,36,54,72,45,54};
                ParkingSlot[] parkingSlots1 = new ParkingSlot[6];

                for (int i = 0; i < plNames.length; i++) {

                    Parkinglot parkinglot = new Parkinglot(plNames[i], plParksPerRow[i],totalParkingSpots[i]);

                    for(int j = 0 ; j<6 ; j++)
                    {
                        parkingSlots1[j] = new ParkingSlot(parkinglot);
                        session.save(parkingSlots1[j]);
                    }
                    parkinglot.setTotalParkingLots(3*3*parkinglot.getParksPerRow());
                    parkinglots.add(parkinglot);

                    session.save(parkinglot);   //saves and flushes to database
                    session.flush();
                }
                CheckedIn checkedIn = new CheckedIn(new Date(), 1234,1234,"test@gmail.com",new Date(),parkingSlots1[1]);
                CheckedIn checkedIn2 = new CheckedIn(new Date(), 5555,12346,"test2@gmail.com",new Date(),parkingSlots1[0]);

                session.save(checkedIn);
                session.save(checkedIn2);
                session.flush();
                session.getTransaction().commit();
                System.out.println("Parkinglots added to database");

            } else { //Get client requests
                session.beginTransaction();
                if (LoginMessage.class.equals(msgClass)) {
                    handler = new LoginHandler((LoginMessage) msg, session, client);
                } else if (ParkingListMessage.class.equals(msgClass)) {
                    handler = new ParkingListHandler((ParkingListMessage) msg, session, client);
                } else if (PricesMessage.class.equals(msgClass)) {
                    handler = new PricesTableHandler((PricesMessage) msg, session, client);
                } else if (OrderMessage.class.equals(msgClass)) {
                    handler = new OrderHandler((OrderMessage) msg, session, client);
                } else if (CheckInGuestMessage.class.equals(msgClass)) {
                    handler = new CheckInHandler((CheckInGuestMessage) msg, session, client);
                }
                if (handler != null) {
                    handler.handleMessage();
                    session.getTransaction().commit();
                    handler.message.message_type = Message.MessageType.RESPONSE;
                    client.sendToClient(handler.message);
                }
            }
        } catch (Exception exception) {
            if(session.getTransaction().isActive())
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
