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

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Properties;


public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    public static Session session;// encapsulation make public function so this can be private

    public SimpleServer(int port) {
        super(port);
//        OrderReminderThread orderReminderThread = new OrderReminderThread();
//        orderReminderThread.start();
        //MembershipReminderThread membershipReminderThread = new MembershipReminderThread();
        //membershipReminderThread.start();
        RemindersThread remindersThread = new RemindersThread();
        remindersThread.start();
        StatisticsThread statisticsThread = new StatisticsThread();
        statisticsThread.start();

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
        configuration.addAnnotatedClass(Statistics.class);
        configuration.addAnnotatedClass(Manager.class);
        configuration.addAnnotatedClass(ParkingLotWorker.class);
        configuration.addAnnotatedClass(CEO.class);
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

                session.beginTransaction();
                generateParkingLots(session);
                session.getTransaction().commit();

                session.beginTransaction();
                generateWorkers(session);
                session.getTransaction().commit();

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
                } else if (CheckOutMessage.class.equals(msgClass)) {
                    handler = new CheckOutHandler((CheckOutMessage) msg, session, client);
                } else if (RegisterUserMessage.class.equals(msgClass)) {
                    handler = new RegisterUserHandler((RegisterUserMessage) msg, session, client);
                } else if (StatisticsMessage.class.equals(msgClass)) {
                    handler = new StatisticsHandler((StatisticsMessage) msg, session, client);
                } else if (CheckInMessage.class.equals(msgClass)) {
                    handler = new CheckInHandler((CheckInMessage) msg, session, client);
                }else if (ComplaintMessage.class.equals(msgClass)) {
                    handler = new ComplaintHandler((ComplaintMessage) msg, session, client);
                }else if (PullParkingSlotsMessage.class.equals(msgClass)) {
                    handler = new PullParkingSlotsHandler((PullParkingSlotsMessage) msg, session, client);
                }
                if (handler != null) {
                    handler.handleMessage();
                    session.getTransaction().commit();
                    handler.message.message_type = Message.MessageType.RESPONSE;
                    if (handler.getClass().equals(PricesTableHandler.class) &&  ( ((PricesMessage) handler.message).response_type.equals(PricesMessage.ResponseType.WAITING_FOR_APPROVAL)
                    || ((PricesMessage) handler.message).response_type.equals(PricesMessage.ResponseType.PRICE_EDITED)))
                    {
                        sendToAllClients(handler.message);
                    }
                    else
                        client.sendToClient(handler.message);
                }
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            if (session != null)
                session.getTransaction().rollback();
            exception.printStackTrace();
        }
    }

    private static void generateParkingLots(Session session) {
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

    public static class RemindersThread extends Thread {
        @Override
        public void run() {
            var yesterday = LocalDate.now().minusDays(1);
            while (true) {
                var session = getSessionFactory().openSession();
//              get all orders which their arrival time was between now and 5 minutes ago and orderStatus is APPROVED
                var orders = session.createQuery("from Order where orderStatus = 'APPROVED' and arrivalTime between :five_minutes_ago and :now")
                        .setParameter("now", LocalDateTime.now())
                        .setParameter("five_minutes_ago", LocalDateTime.now().minusMinutes(5))
                        .getResultList();
                for (Object order : orders) {
                    String email = ((Order) order).getEmail();
                    String subject = "Did you forget your order?";
                    String text = "Hi there, \nYou have an order that you haven't checked in yet.\nWe would " +
                            "like to remind you that in case you are late or don't show up you will be charged according to the terms and conditions of the parking lot." +
                            "\n\nBest regards,\nCarParkSystem";
                    EmailSender.sendEmail(email, subject, text);
                }
//                change the status of the orders to be NOTIFIED
                session.beginTransaction();
                for (Object order : orders) {
//                    update the order status to be NOTIFIED
                    session.createQuery("update Order set orderStatus = :status where id = :id")
                            .setParameter("status", Order.Status.NOTIFIED)
                            .setParameter("id", ((Order) order).getId())
                            .executeUpdate();
                    ((Order) order).setOrderStatus(Order.Status.NOTIFIED);
                    session.update(order);
                }
                session.getTransaction().commit();

//              ---- now for membership reminders ----

                var today = LocalDate.now();
                if(today != yesterday) {
                    yesterday = today;
                    LocalDateTime start = LocalDateTime.of(today, LocalTime.MIN);
                    var memberships = session.createQuery("from Membership where endDate between :now and :seven_days_from_now")
                            .setParameter("now", LocalDateTime.of(today, LocalTime.MIN).plusDays(6))
                            .setParameter("seven_days_from_now", LocalDateTime.of(today, LocalTime.MIN).plusDays(7))
                            .getResultList();
                    //              for each membership get the customerId attribute and get the list of Customer objects having that id
                    for (Object membership : memberships) {
                        //              get the customer object that has the same customerId as the membership
                        var customer = session.createQuery("from Customer where userId = :id")
                                .setParameter("id", ((Membership) membership).getCustomerId())
                                .list();
                        //                  get the email from the customer object
                        String email = ((Customer) customer.get(0)).getEmail();
                        String subject = "Your membership is about to expire";
                        //                    send a text with the expiration date
                        String text = "Hi there, \nWe'd like to inform you that your membership is about to expire on " + ((Membership) membership).getEndDate().toLocalDate() + " at "
                                + ((Membership) membership).getEndDate().toLocalTime() + ".\nYou can login to your" +
                                " account in order to renew it :)" + "\n\nBest regards,\nCarParkSystem";
                        EmailSender.sendEmail(email, subject, text);
                    }
                }

                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                session.close();
            }
        }
    }

    public static class StatisticsThread extends Thread {
        @Override
        public void run() {
            while (true) {
                    var session = getSessionFactory().openSession();
                    var parkingLots = session.createQuery("from Parkinglot").list();
                    for (Object parkingLot : parkingLots) {
//                        check if there is an entry for yesterday
                        String parkingLotId = String.valueOf(((Parkinglot) parkingLot).getParkingLotId());
                        var yesterday = LocalDate.now().minusDays(1);
                        var yesterdayStatistics = session.createQuery("from Statistics where parkingLotId = :parkingLotId and date = :date")
                                .setParameter("parkingLotId", parkingLotId)
                                .setParameter("date", yesterday)
                                .getResultList();
                        if (yesterdayStatistics.size() == 0) {
                            //                        select all orders from the begiining of yesterday to the end of yesterday
//                            wrap yesterday in a LocalDateTime object
                            LocalDateTime yesterdayStart = LocalDateTime.of(yesterday, LocalTime.MIN);
                            LocalDateTime yesterdayEnd = LocalDateTime.of(yesterday, LocalTime.MAX);
                            var orders = session.createQuery("from Order where parkingLot = :parkingLotId and arrivalTime between :yesterday_start and :yesterday_end")
                                    .setParameter("parkingLotId", parkingLotId)
                                    .setParameter("yesterday_start", yesterdayStart)
                                    .setParameter("yesterday_end", yesterdayEnd)
                                    .getResultList();

                            int totalOrders = orders.size();
                            int numberOfOrdersCancelled = 0;
                            int numberOfOrdersLate = 0;
                            int totalRevenue = 0;
                            for (Object order : orders) {
                                totalRevenue += ((Order) order).getOrdersPrice();
                                switch (((Order) order).getStatus()) {
                                    case APPROVED:
                                        break;
                                    case NOTIFIED:
                                        numberOfOrdersLate++;
                                        break;
                                    case CANCELLED:
                                        numberOfOrdersCancelled++;
                                        break;
                                }
                            }
                            Statistics statistics = new Statistics(yesterday, totalOrders, numberOfOrdersCancelled, numberOfOrdersLate, parkingLotId, totalRevenue);
                            session.beginTransaction();
//                            save the statistics to the table
                            session.save(statistics);
                            session.getTransaction().commit();
                        }
                    }
//                    delete all expired memberships from the database
                    var expiredMemberships = session.createQuery("from Membership where endDate < :now")
                            .setParameter("now", LocalDateTime.now())
                            .getResultList();
                    session.beginTransaction();
                    for (Object membership : expiredMemberships) {
                        session.delete(membership);
                    }
//                    Have to add 'CHECKED_OUT' as a status for orders
//                    var expiredOrders = session.createQuery("from Order where orderStatus = 'CHECKED_OUT'")
//                            .getResultList();
//                    for (Object order : expiredOrders) {
//                        session.delete(order);
//                    }
                    session.getTransaction().commit();
                try {
                    Thread.sleep(86400000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                session.close();
            }
        }
    }




    public static class EmailSender {
        public static void sendEmail(String to, String subject, String text) {
            String from = "ModernParkingSolutionsCPS@outlook.com";

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp-mail.outlook.com");
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.debug", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(properties, null);

            try {
                javax.mail.Message msg = new MimeMessage(mailSession);
                msg.setFrom(new InternetAddress(from));
                msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
                msg.setSubject(subject);
                msg.setText(text);

                // Send the msg to the recipient.
                Transport.send(msg, "ModernParkingSolutionsCPS@outlook.com", "cpsteam4");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateWorkers(Session session) throws Exception {
        CriteriaBuilder cb;
        cb = session.getCriteriaBuilder();
        CriteriaQuery<Parkinglot> query = cb.createQuery(Parkinglot.class);
        query.from(Parkinglot.class);
        List<Parkinglot> data = session.createQuery(query).getResultList();
        byte[] salt = HashPipeline.getSalt();

        Manager daniel = new Manager("318172848","Daniel","Glazman","glazman.daniel@gmal.com","Manager",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        daniel.setParkinglot(data.get(0));
        session.save(daniel);
        session.flush();

        Manager avi = new Manager("209042589","Avi","Lifshitz","vilifishitz@gmal.com","Manager",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        avi.setParkinglot(data.get(1));
        session.save(avi);
        session.flush();

        Manager noy = new Manager("207944414","Noy","Blitsblau","oybl101@gmal.com","Manager",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        noy.setParkinglot(data.get(2));
        session.save(noy);
        session.flush();

        Manager shahar = new Manager("314983040","Shahar","Weiss","shaharweiss0@gmal.com","Manager",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        shahar.setParkinglot(data.get(3));
        session.save(shahar);
        session.flush();

        Manager eliron = new Manager("313313131","Eliron","Lubaton","lubaton@gmal.com","Manager",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        eliron.setParkinglot(data.get(4));
        session.save(eliron);
        session.flush();

        CEO yuval = new CEO("313598484","Yuval","Fisher","fisheryuval96@gmal.com","CEO",
                HashPipeline.toHexString(HashPipeline.getSHA("1111111", salt)), salt);
        session.save(yuval);
        session.flush();

        ParkingLotWorker parkingLotWorker1 = new ParkingLotWorker("098765432", "Regina", "Phalange", "regina@gmail.com", "Parking Lot Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("1234567", salt)), salt);
        parkingLotWorker1.setParkinglot(data.get(0));
        session.save(parkingLotWorker1);
        session.flush();

        ParkingLotWorker parkingLotWorker2 = new ParkingLotWorker("213243546", "Chandler", "Bing", "chandler@gmail.com", "Parking Lot Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("1234567", salt)), salt);
        parkingLotWorker2.setParkinglot(data.get(1));
        session.save(parkingLotWorker2);
        session.flush();

        ParkingLotWorker parkingLotWorker3 = new ParkingLotWorker("222111343", "Phoebe", "Boffay", "Phoebe@gmail.com", "Parking Lot Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("1234567", salt)), salt);
        parkingLotWorker3.setParkinglot(data.get(2));
        session.save(parkingLotWorker3);
        session.flush();

        ParkingLotWorker parkingLotWorker4 = new ParkingLotWorker("333222111", "Monica", "Geller", "Monica@gmail.com", "Parking Lot Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("1234567", salt)), salt);
        parkingLotWorker4.setParkinglot(data.get(3));
        session.save(parkingLotWorker4);
        session.flush();

        ParkingLotWorker parkingLotWorker5 = new ParkingLotWorker("123123123", "Ross", "Geller", "Ross@gmail.com", "Parking Lot Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("1234567", salt)), salt);
        parkingLotWorker5.setParkinglot(data.get(4));
        session.save(parkingLotWorker5);
        session.flush();

        Employee customerService1 = new Employee("099888999", "Joey", "Tribbiani", "Joey@gmail.com", "Customer Service Worker",
                HashPipeline.toHexString(HashPipeline.getSHA("123456789", salt)), salt);
        session.save(customerService1);
        session.flush();

    }

    public void sendToAllClients(Message message) {
        try {
            for (SubscribedClient SubscribedClient : SubscribersList) {
                SubscribedClient.getClient().sendToClient(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
