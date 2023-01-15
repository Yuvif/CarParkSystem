package CarPark.server.handlers;

import CarPark.entities.Complaint;
import CarPark.entities.messages.ComplaintMessage;
import CarPark.entities.messages.Message;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
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
        }
    }

    private void createComplaint() {
        Complaint newComplaint = class_message.complaints.get(0) ;
        session.save(newComplaint);
        session.flush();
    }

    private List<Complaint> getComplaintList() throws Exception {
        CriteriaQuery<Complaint> query = cb.createQuery(Complaint.class);
        query.from(Complaint.class);
        List<Complaint> data = session.createQuery(query).getResultList();
        return data;
    }
}
