package CarPark.server;
import CarPark.entities.*;
import CarPark.entities.messages.*;
import CarPark.server.handlers.*;
import CarPark.server.ocsf.AbstractServer;
import CarPark.server.ocsf.ConnectionToClient;
import CarPark.server.ocsf.SubscribedClient;
import com.textmagic.sdk.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;


public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    public static Session session;// encapsulation make public function so this can be private

    public SimpleServer(int port) throws Exception {
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
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Membership.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }
    static List<Parkinglot> getAllParkingLots() throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Parkinglot> query = builder.createQuery(Parkinglot.class);
        query.from(Parkinglot.class);
        List<Parkinglot> data = session.createQuery(query).getResultList();
        LinkedList<Parkinglot> list = new LinkedList<Parkinglot>();
        for (Parkinglot parkinglot : data) {     //converts arraylist to linkedlist
            list.add(parkinglot);
        }
        return list;
    }


    private static List<Complaint> generateComplaints(List<Parkinglot> p) throws Exception {       //generates new products
        List<Complaint> complaints = new LinkedList<Complaint>();
        int parkinglotN = 0;
        String[] complaintsDiscription = new String[]{
                "Hello, I ordered 2 tulips but got only 1. I'd like to get refunded for that.",
                "Dear Customer Support, I tried to buy with my visa and it didn't work, and then after multiple tries it charged me twice.",
                "Hello there, I ordered from your chain, and didn't receive what I wanted.",
                "Hello there, I ordered from your chain, and didn't receive what I desired."};
        for (int i = 0; i < complaintsDiscription.length; i++) {
            if (i < p.size())
                parkinglotN = i % p.size();
            if (i < complaintsDiscription.length) {
                int rand = new Random().nextInt(30) + 1;
                Date d = new Date();
                Date date = new Date(d.getTime() - Duration.ofDays(i % rand).toMillis());
                Complaint comp = new Complaint(date, complaintsDiscription[i], p.get(parkinglotN).getId());
                complaints.add(comp);
                session.save(comp);
                session.flush();
            }
        }
        return complaints;
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
                } else if (RegisterMessage.class.equals(msgClass)) {
                    handler = new RegisterHandler((RegisterMessage) msg, session, client);
                } else if (OrdersTableMessage.class.equals(msgClass)) {
                    handler = new OrdersTableHandler((OrdersTableMessage) msg, session, client);
                }
                else if (ParkingSlotsMessage.class.equals(msgClass)) {
                    handler = new EditParkingSlotsHandler((ParkingSlotsMessage) msg, session, client);
                }else if (PullParkingSlotsMessage.class.equals(msgClass)) {
                    handler = new PullParkingSlotsHandler((PullParkingSlotsMessage) msg, session, client);
                }else if (PullOrdersMessage.class.equals(msgClass)) {
                    handler = new PullOrdersHandler((PullOrdersMessage) msg, session, client);
                }else if (ComplaintMessage.class.equals(msgClass)) {
                    //---------------------
                    generateComplaints(getAllParkingLots());
                    handler = new ComplaintHandler((ComplaintMessage) msg, session, client);
                }else if (RegisterUserMessage.class.equals(msgClass))
                    handler = new RegisterUserHandler((RegisterUserMessage)msg,session,client);
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
}
