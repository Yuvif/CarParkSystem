package CarPark.server.handlers;

import CarPark.entities.Customer;
import CarPark.entities.messages.Message;
import CarPark.entities.messages.RegisterUserMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class RegisterUserHandler extends MessageHandler {
        private final RegisterUserMessage class_message;


    public RegisterUserHandler(Message msg, Session session, ConnectionToClient client) {
        super(msg, session, client);
        this.class_message = (RegisterUserMessage) this.message;
    }

    private List<Customer> getUsersList() throws Exception
        {
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            query.from(Customer.class);
            List<Customer> data = session.createQuery(query).getResultList();
            return data;
        }

        private void createUser() throws Exception
        {
            Customer new_customer = class_message.newCustomer;
            List<Customer> customers = getUsersList();

            for (Customer customer : customers)
            {
                if (customer.getId().equals(new_customer.getId()) || customer.getEmail() == new_customer.getEmail())
                {
                    class_message.response_type = RegisterUserMessage.ResponseType.REGISTRATION_FAILED;
                    break;
                }
            }

            if(class_message.response_type != RegisterUserMessage.ResponseType.REGISTRATION_FAILED)
            {
                class_message.response_type = RegisterUserMessage.ResponseType.REGISTRATION_SUCCEEDED;
                session.save(new_customer);
                session.flush();
            }
        }

        @Override
        public void handleMessage() throws Exception
        {
            switch (class_message.request_type) {
                case REGISTER:
                    createUser();
                    break;

            }
        }
    }

