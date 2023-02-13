package CarPark.server.handlers;

import CarPark.entities.Complaint;
import CarPark.entities.Customer;
import CarPark.entities.Parkinglot;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import CarPark.server.SimpleServer;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ComplaintHandler extends MessageHandler {

    private final ComplaintMessage class_message;
    private Complaint inspectedComplaint;

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
                class_message.complaints = getComplaintList();
                class_message.response_type = ComplaintMessage.ResponseType.SET_ALL_COMPLAINTS;
                break;

            case COMPENSATE_COMPLAINT:
                class_message.complaint2handle.setStatus(true);
                updateComplaintStatus();
                compensateCustomer();
//                if(class_message.amount > 0) {
//                    SimpleServer.EmailSender.sendEmail(class_message.complaint2handle.getCustomer().getEmail(),
//                            "Hi there,\nYour complaint from " + class_message.complaint2handle.getDate() + " has been processed!\n" +
//                                    "You have been compensated in the amount of " + class_message.amount + "₪\n" +
//                                    "Have a good day!\nCar Park System",
//                            "Your Complaint Was Processed");
//                } else {
//                    SimpleServer.EmailSender.sendEmail(class_message.complaint2handle.getCustomer().getEmail(),
//                            "Hi there,\nYour complaint has been rejected and no refund has been issued!\n" +
//                                    "Our apologize,\nCar Park System",
//                            "Your Complaint Was Processed");
//                }

                class_message.response_type = ComplaintMessage.ResponseType.COMPLAINT_CLOSED;
                break;

            case GET_MY_COMPLAINTS:
                getMyComplaints();
                class_message.response_type = ComplaintMessage.ResponseType.SET_MY_COMPLAINTS;
                break;

            case GET_OPEN_COMPLAINT:
                this.inspectedComplaint = class_message.complaint2handle;
                class_message.response_type = ComplaintMessage.ResponseType.SET_DISPLAY_COMPLAINT;
                break;

            case GET_COMPLAINTS_REP:
                class_message.complaints2Rep = getComplaintRep();
                class_message.response_type = ComplaintMessage.ResponseType.SET_ALL_COMPLAINTS;
                break;
        }
    }


    private void createComplaint() {
        String hql = "FROM Parkinglot WHERE name = :pl_name";
        Query query = session.createQuery(hql);
        query.setParameter("pl_name", class_message.complaint2handle.getPl_name());
        class_message.complaint2handle.setParkinglot((Parkinglot) query.getSingleResult());

        Complaint newComplaint = class_message.complaint2handle ;
        Random rand = new Random();
        newComplaint.setComplaintId(rand.nextInt(99999 + 1 - 10000) + 10000); //generate 5 digit number
        session.save(newComplaint);
        session.flush();
    }

    private List<Complaint> getComplaintList() throws Exception {
        CriteriaQuery<Complaint> query = cb.createQuery(Complaint.class);
        query.from(Complaint.class);
        List<Complaint> data = session.createQuery(query).getResultList();
        List<Complaint> res = new ArrayList<Complaint>();


            for (Complaint c : data) {
                if (!c.getAppStatus()) {
                    assert res != null;
                    res.add(c);
                }
            }

        return res;
    }

    public void getMyComplaints() throws Exception {
        String hql = "FROM Complaint WHERE customer = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", class_message.current_customer.getId());
        class_message.complaints = query.getResultList();
        class_message.response_type= ComplaintMessage.ResponseType.SET_MY_COMPLAINTS;
    }

    public void updateComplaintStatus()
    {
        String hql = "UPDATE Complaint SET appStatus = :status WHERE complaintId = :complaintID";
        Query query = session.createQuery(hql);
        query.setParameter("status", class_message.complaint2handle.getAppStatus());
        query.setParameter("complaintID", class_message.complaint2handle.getComplaintId());

        session.evict(class_message.complaint2handle);
        query.executeUpdate();

        session.merge(class_message.complaint2handle);
        session.flush();
    }

    public void compensateCustomer()
    {
        Customer current_customer = session.get(Customer.class, class_message.complaint2handle.getCustomer().getId());
        current_customer.addToBalance(-1 * class_message.amount);
        session.flush();
    }

    public LinkedList<Complaint> getComplaintRep() {
        CriteriaQuery<Complaint> query = cb.createQuery(Complaint.class);
        query.from(Complaint.class);
        List<Complaint> data = session.createQuery(query).getResultList();
        LinkedList<Complaint> res = new LinkedList<>(data);
        return res;
    }
}