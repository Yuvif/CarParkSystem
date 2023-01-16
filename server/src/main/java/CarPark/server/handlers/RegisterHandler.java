package CarPark.server.handlers;

import CarPark.entities.Membership;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.RegisterMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Random;

public class RegisterHandler extends MessageHandler{

    private RegisterMessage class_message;

    public RegisterHandler(Message msg, Session session, ConnectionToClient client)
    {
        super(msg, session, client);
        this.class_message = (RegisterMessage) this.message;
    }

    private List<Membership> getMembershipList() throws Exception
    {
        CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
        query.from(Membership.class);
        List<Membership> data = session.createQuery(query).getResultList();
        return data;
    }

    private void createMembership() throws Exception
    {
        Membership newMembership = class_message.newMembership;
        List<Membership> existingMemberships = getMembershipList();

        for (Membership membership : existingMemberships)
        {
            if (membership.getCarId() == newMembership.getCarId() && membership.getCustomerId() == newMembership.getCustomerId())
            {
                class_message.response_type = RegisterMessage.ResponseType.REGISTRATION_FAILED;
                break;
            }
        }

        if(class_message.response_type != RegisterMessage.ResponseType.REGISTRATION_FAILED)
        {
            Random rand = new Random();
            newMembership.setMembershipId(rand.nextInt(99999 + 1 - 10000) + 10000); //generate 5 digit membership number
            class_message.newMembership = newMembership;
            class_message.response_type = RegisterMessage.ResponseType.REGISTRATION_SUCCEEDED;
            session.save(newMembership);
            session.flush();
        }
    }

    @Override
    public void handleMessage() throws Exception
    {
        switch (class_message.request_type) {
            case REGISTER:
                createMembership();
                break;
        }
    }


}
