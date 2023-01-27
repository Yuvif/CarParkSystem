package CarPark.server.handlers;

import CarPark.entities.Complaint;
import CarPark.entities.Customer;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ComplaintHandler extends MessageHandler {

    private final ComplaintMessage class_message;

    public ComplaintHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (ComplaintMessage) this.message;
    }

    @Override
    public void handleMessage() throws Exception {
        switch (class_message.request_type) {
            case CREATE_NEW_COMPLAINT:
                createComplaint();
                class_message.response_type = ComplaintMessage.ResponseType.COMPLAINT_SUBMITTED;
                break;
            case GET_ALL_COMPLAINTS:
                generateCustomers();
                class_message.complaints2Rep = getComplaintList();
                class_message.response_type = ComplaintMessage.ResponseType.SET_ALL_COMPLAINTS;
                break;
            case COMPENSATE_COMPLAINT:
                //class_message.complaint2handle.user.setBalance(class_message.amount);
               // class_message.complaint2handle.getCustomerId().setBalance(class_message.amount);
                break;
            case GET_MY_COMPLAINTS:
               getMyComplaints();
               break;
        }
    }

    private void createComplaint() {
        Complaint newComplaint = class_message.complaint2handle ;
        Random rand = new Random();
        newComplaint.setComplaintId(rand.nextInt(99999 + 1 - 10000) + 10000); //generate 5 digit membership number
        session.save(newComplaint);
        session.flush();
    }

    private LinkedList<Complaint> getComplaintList() throws Exception {
        System.out.println("complaint handler");
        CriteriaQuery<Complaint> query = cb.createQuery(Complaint.class);
        query.from(Complaint.class);
        List<Complaint> data = session.createQuery(query).getResultList();
        LinkedList<Complaint> res= new LinkedList<Complaint>();
        for(Complaint c: data)
        {
            res.add(c);
        }
        return res;
    }

    public void getMyComplaints() throws Exception {
        String hql = "FROM Complaint WHERE customerId = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        class_message.complaints = query.getResultList();
        class_message.response_type= ComplaintMessage.ResponseType.SET_MY_COMPLAINTS;
    }

    private void generateCustomers() throws Exception {
        Customer customer1 = new Customer(318172848,"Daniel","Glazman","glazman.daniel@gmail.com",100.0,"1234567");
        session.save(customer1);
        session.flush();
        Customer customer2 = new Customer(313598484,"Yuval","Fisher","fisheryuval96@gmail.com",50.5, "7777777");
        session.save(customer2);
        session.flush();
    }

}
