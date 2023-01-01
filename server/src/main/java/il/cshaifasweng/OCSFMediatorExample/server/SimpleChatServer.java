package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.Parkinglot;
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

//logic infront of the db
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
        configuration.addAnnotatedClass(Parkinglot.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();        //pull session factory config from hibernate properties
        return configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     *
     * @return all opened complaints in the database.
     * @throws IOException
     */
    //query:
    static List<Complaint> getAllOpenComplaints() throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Complaint> customerQuery = builder.createQuery(Complaint.class);
        customerQuery.from(Complaint.class);
        List<Complaint> complaints = session.createQuery(customerQuery).getResultList();
        complaints.removeIf(complaint -> !complaint.getStatus());
        return new LinkedList<>(complaints);
    }

//    static List<Parkinglot> getAllParkingLots() throws IOException {
////
////        CriteriaBuilder builder = session.getCriteriaBuilder();
////        CriteriaQuery<Parkinglot> query = builder.createQuery(Parkinglot.class);
////        query.from(Parkinglot.class);
////        List<Parkinglot> data = session.createQuery(query).getResultList();
////        LinkedList<Parkinglot> list = new LinkedList<Parkinglot>();
////        for (Parkinglot parkinglot : data) {     //converts arraylist to linkedlist
////            list.add(parkinglot);
////        }
////        return list;
////    }
    static List<Parkinglot> getAllParkingLots() throws IOException {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Parkinglot> customerQuery = builder.createQuery(Parkinglot.class);
        customerQuery.from(Parkinglot.class);
        List<Parkinglot> parkinglots = session.createQuery(customerQuery).getResultList();
        return new LinkedList<>(parkinglots);
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
//            Complaint a = new Complaint(new Date(), "Ad Matay");
//            Complaint b = new Complaint(new Date(), "blabla");
//            Complaint c = new Complaint(new Date(), "Kama od");

            Parkinglot parkinglot1 = new Parkinglot("Eilat", 5,10);
            Parkinglot parkinglot2 = new Parkinglot("Haifa", 5,10);
            Parkinglot parkinglot3 = new Parkinglot("Binyamina", 5,10);

//            session.save(a);
//            session.save(b);
//            session.save(c);
            session.save(parkinglot1);
            session.save(parkinglot2);
            session.save(parkinglot3);
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
