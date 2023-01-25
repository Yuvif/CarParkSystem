package CarPark.server.handlers;

import CarPark.entities.Complaint;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import java.util.LinkedList;
import java.util.List;

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
        session.save(newComplaint);
        session.flush();
    }

    private LinkedList<Complaint> getComplaintList() throws Exception {
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
}
