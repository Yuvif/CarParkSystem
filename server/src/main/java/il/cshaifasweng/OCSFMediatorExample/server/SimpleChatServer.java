package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
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
        configuration.addAnnotatedClass(ParkingSlot.class);
        configuration.addAnnotatedClass(CheckedIn.class);

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


    private static void generateEntities() throws Exception {       //generates all entities
        //--------------------Parking Lots-----------------------------------------------------
        List<Parkinglot> parkinglots = new LinkedList<Parkinglot>();
        parkinglots = generateParkinglots();

//        //--------------------Complaints-----------------------------------------------------
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




    private static List<Parkinglot> generateParkinglots() throws Exception {       //generates new Parkinglots
        List<Parkinglot> parkinglots = new LinkedList<Parkinglot>();
        String[] plNames = new String[]{"CPS Haifa", "CPS Tel-Aviv", "CPS Be'er Sheva", "CPS Rehovot", "CPS Jerusalem", "CPS Eilat"};
        int[] plParksPerRow = new int[]{5,4,6,8,5,6};
        int[] totalParkingSpots = new int[]{45,36,54,72,45,54};
        ParkingSlot[] parkingSlots = new ParkingSlot[6];

        for (int i = 0; i < plNames.length; i++) {

            Parkinglot parkinglot = new Parkinglot(plNames[i], plParksPerRow[i],totalParkingSpots[i]);

            for(int j = 0 ; j<6 ; j++)
            {
                parkingSlots[j] = new ParkingSlot(parkinglot);
                parkinglot.addParkingSlots(parkingSlots[j]);
                session.save(parkingSlots[j]);
            }
            parkinglot.setTotalParkingLots(3*3*parkinglot.getParksPerRow());
            parkinglots.add(parkinglot);

            session.save(parkinglot);   //saves and flushes to database
            session.flush();
        }
        CheckedIn checkedIn = new CheckedIn(new Date(), 1234,1234,"test",new Date(),parkingSlots[2]);
        session.save(checkedIn);
        session.flush();
        return parkinglots;
    }


    public static void main(String[] args) throws IOException {
        try {

            SessionFactory sessionFactory = getSessionFactory();        //calls and creates session factory
            session = sessionFactory.openSession(); //opens session
            session.beginTransaction();       //transaction for generation
            generateEntities();             //generate


//            LocalDateTime[] entryTime = new LocalDateTime[6];
//            entryTime[0] = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33);
//            entryTime[1] = LocalDateTime.of(2019, Month.MARCH, 28, 14, 37);
//            entryTime[2] = LocalDateTime.of(2019, Month.MARCH, 28, 14, 39);
//            entryTime[3] = LocalDateTime.of(2019, Month.MARCH, 28, 14, 42);
//            entryTime[4] = LocalDateTime.of(2019, Month.MARCH, 28, 14, 45);
//            entryTime[5] = LocalDateTime.of(2019, Month.MARCH, 28, 15, 55);
//
//
//            LocalDateTime[] exitTime = new LocalDateTime[6];
//            exitTime[0] = LocalDateTime.of(2019, Month.MARCH, 28, 18, 33);
//            exitTime[1] = LocalDateTime.of(2019, Month.MARCH, 28, 16, 37);
//            exitTime[2] = LocalDateTime.of(2019, Month.MARCH, 28, 19, 39);
//            exitTime[3] = LocalDateTime.of(2019, Month.MARCH, 29, 14, 42);
//            exitTime[4] = LocalDateTime.of(2019, Month.MARCH, 28, 22, 45);
//            exitTime[5] = LocalDateTime.of(2019, Month.MARCH, 28, 16, 35);


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
