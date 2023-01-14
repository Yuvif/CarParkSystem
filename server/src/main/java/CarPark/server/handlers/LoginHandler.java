package CarPark.server.handlers;
import CarPark.entities.Customer;
import CarPark.entities.Employee;
import CarPark.entities.messages.LoginMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.Query;


public class LoginHandler extends MessageHandler {
    private final LoginMessage class_message;

    public LoginHandler(LoginMessage message, Session session, ConnectionToClient client) {
        super(message, session, client);
        this.class_message = (LoginMessage) this.message;
    }

    @Override
    public void handleMessage() {
        Employee employee = null;
        Customer customer = null;
        String hql = "FROM User WHERE userId = :user_id";
        Query query = session.createQuery(hql);
        query.setParameter("pass",class_message.getPassword());
        query.setParameter("user_id",class_message.getUserId());
        employee = (Employee)query.getSingleResult();
        if (employee != null) {
            class_message.employee = employee;
            class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_EMPLOYEE;
        }
        else {
            hql = "SELECT * FROM Customer WHERE password = :pass AND customerId = :user_id";
            query = session.createQuery(hql);
            query.setParameter("pass",class_message.getPassword());
            query.setParameter("user_id",class_message.getUserId());
            customer = (Customer) query.getSingleResult();
            if (customer != null) {
                class_message.customer = customer;
                class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_CUSTOMER;
            }
            else
            {
                class_message.response_type = LoginMessage.ResponseType.LOGIN_FAILED;
            }
        }
    }


}
