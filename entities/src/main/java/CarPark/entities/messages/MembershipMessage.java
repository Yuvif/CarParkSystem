package CarPark.entities.messages;

import CarPark.entities.Customer;
import CarPark.entities.Membership;

import java.util.List;

public class MembershipMessage extends Message{

    public RequestType request_type;
    public ResponseType response_type;
    public Membership newMembership;
    public List<Membership> memberships;
    public Customer current_customer;

    public MembershipMessage(MessageType request, RequestType request_type , Customer customer) {
        super(request);
        this.request_type = request_type;
        this.current_customer = customer;
    }

    public enum RequestType {
        GET_MY_MEMBERSHIPS,
        REGISTER
    }

    public enum ResponseType {
        REGISTRATION_FAILED,
        REGISTRATION_SUCCEEDED,
        SEND_TABLE
    }

    public MembershipMessage(MessageType message_type, RequestType request_type, Membership membership, Customer customer)
    {
        super(message_type);
        this.request_type = request_type;
        this.newMembership = membership;
        this.current_customer = customer;
    }

    public MembershipMessage(MessageType message_type, MembershipMessage.ResponseType response_type) {
        super(message_type);
        this.response_type = response_type;
    }

}
