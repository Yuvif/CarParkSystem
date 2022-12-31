package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class SimpleChatServer
{
    public static Session session;// encapsulation make public function so this can be private
    protected static SimpleServer server;

    /**
     * Creates session factory for database use
     * @return session factory.
     * @throws HibernateException
     */
    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.

        configuration.addAnnotatedClass(Complaint.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     *
     * @return all opened complaints in the database.
     * @throws IOException
     */
    static List<Complaint> getAllOpenComplaints() throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Complaint> customerQuery = builder.createQuery(Complaint.class);
        customerQuery.from(Complaint.class);
        List<Complaint> complaints = session.createQuery(customerQuery).getResultList();
        complaints.removeIf(complaint -> !complaint.getStatus());
        return new LinkedList<>(complaints);
    }
//    A function that pulls from the db all orders that belong to a client
    static List<Order> getAllOrdersOfCustomer(int clientId) throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> customerQuery = builder.createQuery(Order.class);
        customerQuery.from(Order.class);
        List<Order> orders = session.createQuery(customerQuery).getResultList();
        orders.removeIf(order -> order.getCustomerId() != clientId);
        return new LinkedList<>(orders);
    }
//    A function that returns all orders in a specific parking lot
    static List<Order> getAllOrdersOfParkingLot(int parkingLotId) throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Order> customerQuery = builder.createQuery(Order.class);
        customerQuery.from(Order.class);
        List<Order> orders = session.createQuery(customerQuery).getResultList();
        orders.removeIf(order -> order.getParkingLotId() != parkingLotId);
        return new LinkedList<>(orders);
    }

//    private static void generateEntities() throws Exception {       //generates all entities
//        List<Complaint> complaints = new LinkedList<Complaint>();
//        Complaint complaint = new Complaint(new Date(), "test");
//
//        complaints.add(complaint);
//    }

//    public static void main( String[] args ) throws IOException
//    {
//        server = new SimpleServer(3000);
//        System.out.println("server is listening");
//        server.listen();
//    }
    public static void main(String[] args) throws IOException {
        try {

            SessionFactory sessionFactory = getSessionFactory();        //calls and creates session factory
            session = sessionFactory.openSession(); //opens session
            session.beginTransaction();       //transaction for generation
//            generateEntities();             //generate
//            //generateStores();
//             TEMP**********************************************************
            Complaint a = new Complaint(new Date(), "Ad Matay");
            Complaint b = new Complaint(new Date(), "blabla");
            Complaint c = new Complaint(new Date(), "Kama od");
            session.save(a);
            session.save(b);
            session.save(c);
            session.flush();
            session.getTransaction().commit(); // Save everything.

//            ScheduleMailing.main(null);

            server = new SimpleServer(3000);      //builds server
            server.listen();                    //listens to client
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            e.printStackTrace();
        } finally {
            if (session != null) {
                while (!server.isClosed()) ;
                session.close();
                session.getSessionFactory().close();
            }
        }
}}
