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
    private OrderReminderThread orderReminderThread;
    private MembershipReminderThread membershipReminderThread;

    public SimpleServer(int port) {
        super(port);
//        OrderReminderThread orderReminderThread = new OrderReminderThread();
//        orderReminderThread.start();
//        MembershipReminderThread membershipReminderThread = new MembershipReminderThread();
//        membershipReminderThread.start();
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
                } else if (MembershipMessage.class.equals(msgClass)) {
                    handler = new MembershipsHandler((MembershipMessage) msg, session, client);
                } else if (ParkingLotMapMessage.class.equals(msgClass)) {
                    handler = new ParkingLotMapHandler((ParkingLotMapMessage) msg, session, client);
                }else if (CheckOutMessage.class.equals(msgClass)) {
                handler = new CheckOutHandler((CheckOutMessage) msg, session, client);
                }
                else if (RegisterUserMessage.class.equals(msgClass))
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

    public static class OrderReminderThread extends Thread {
        @Override
        public void run() {
            var session = getSessionFactory().openSession();
            while (true) {
//              get all orders that are at least 2 minutes late
                var orders = session.createQuery("from Order where arrivalTime = :time and orderStatus = :status")
                        .setParameter("time", LocalDateTime.now().minusMinutes(2))
                        .setParameter("status", Order.Status.APPROVED)
                        .list();
                for (Object order : orders) {
                    String email = ((Order) order).getEmail();
                    String subject = "Did you forget your order?";
                    String text = "Hi, \nyou have an order that you haven't checked in yet and we would " +
                            "like to remind you that in case you are late or don't show up you will be charged according to the terms and conditions of the parking lot.\n\nBest regards,\nCarPark";
                    EmailSender.sendEmail(email, subject, text);
                }
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class MembershipReminderThread extends Thread {
        @Override
        public void run() {
            while (true) {
                var session = getSessionFactory().openSession();
                var memberships = session.createQuery("from Membership where endDate = :time")
                        .setParameter("time", LocalDateTime.now().plusDays(7))
                        .list();
//              for each membership get the customerId attribute and get the list of Customer objects having that id
                for (Object membership : memberships) {
//              get the customer object that has the same customerId as the membership
                    var customer = session.createQuery("from Customer where userId = :id")
                            .setParameter("id", ((Membership) membership).getCustomerId())
                            .list();
//                  get the email from the customer object
                    String email = ((Customer) customer.get(0)).getEmail();
                    String subject = "Your membership is about to expire";
                    String text = "Hi, \nyour membership is about to expire in a week and we would like " +
                            "to remind you that in case you are late or don't show up you will be charged according to the terms and conditions of the parking lot.\n\nBest regards,\nCarPark";
                    EmailSender.sendEmail(email, subject, text);
                }
                // now wait for 7 days
                try {
                    Thread.sleep(604800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class EmailSender{
        public static void sendEmail(String to, String subject, String text) {
            String from = "ModernParkingSolutionsCPS@outlook.com";

	*/


}
