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
import java.util.Arrays;
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
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Membership.class);
        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(ParkingSlot.class);
        configuration.addAnnotatedClass(CheckedIn.class);
        // configuration.addAnnotatedClass(ParkingLotWorker.class);

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
                //session.beginTransaction();
                //generateParkingLots();
                //session.getTransaction().commit();
            } else { //Get client requests
                session.beginTransaction();
                if (LoginMessage.class.equals(msgClass)) {
                    handler = new LoginHandler((LoginMessage) msg, session, client);
                } else if (ComplaintMessage.class.equals(msgClass)) {
                    handler = new ComplaintHandler((ComplaintMessage) msg, session, client);
                } else if (ParkingListMessage.class.equals(msgClass)) {
                    handler = new ParkingListHandler((ParkingListMessage) msg, session, client);
                } else if (PricesMessage.class.equals(msgClass)) {
                    handler = new PricesTableHandler((PricesMessage) msg, session, client);
                } else if (MembershipMessage.class.equals(msgClass)) {
                    handler = new MembershipsHandler((MembershipMessage) msg, session, client);
                } else if (OrderMessage.class.equals(msgClass)) {
                    handler = new OrderHandler((OrderMessage) msg, session, client);
                } else if (RegisterUserMessage.class.equals(msgClass))
                    handler = new RegisterUserHandler((RegisterUserMessage) msg, session, client);
                if (handler != null) {
                    handler.handleMessage();
                    session.getTransaction().commit();
                    if (!(handler.getClass().equals(LoginHandler.class) && ((LoginMessage) msg).request_type.equals(LoginMessage.RequestType.LOGOUT))) {
                        handler.message.message_type = Message.MessageType.RESPONSE;
                        client.sendToClient(handler.message);
                    }
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

    private void generateParkingLots() {
        String current_id;
        List<Parkinglot> parkingLotList = new LinkedList<>();
        Parkinglot haifa = new Parkinglot("Haifa", 4, 36);
        session.save(haifa);
        session.flush();
        parkingLotList.add(haifa);
        Parkinglot tlv = new Parkinglot("Tel Aviv", 7, 63);
        session.save(tlv);
        session.flush();
        parkingLotList.add(tlv);
        Parkinglot jerusalem = new Parkinglot("Jerusalem", 8, 72);
        session.save(jerusalem);
        session.flush();
        parkingLotList.add(jerusalem);
        Parkinglot bash = new Parkinglot("Be'er Sheva", 6, 54);
        session.save(bash);
        session.flush();
        parkingLotList.add(bash);
        Parkinglot eilat = new Parkinglot("Eilat", 5, 45);
        session.save(eilat);
        session.flush();
        parkingLotList.add(eilat);
        List<String> spots = Arrays.asList("A", "B", "C");
        int f = 1; //floor number
        int currentSpot = 1; //current spot number
        for (int i = 0; i < parkingLotList.size(); i++) {
            Parkinglot parkinglot = parkingLotList.get(i);
            int spotIndex = 0;
            int carsPerFloor = parkinglot.getTotalParkingSlots() / 3; //set the number of cars per floor here
            for (int j = 0; j < parkinglot.getTotalParkingSlots(); j++) {
                String currentId = f + "." + spots.get(spotIndex) + currentSpot;
                ParkingSlot parkingSlot = new ParkingSlot(currentId, parkinglot);
                session.save(parkingSlot);
                session.flush();
                currentSpot++;
                if (currentSpot > carsPerFloor) {
                    currentSpot = 1;
                    spotIndex++;
                    if (spotIndex >= spots.size()) {
                        spotIndex = 0;
                        f++;
                    }
                }
            }
        }
    }
}
