package CarPark.server.handlers;

import CarPark.entities.Customer;
import CarPark.entities.User;
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

    private List<User> getUsersList() throws Exception
        {
            CriteriaQuery<User> query = cb.createQuery(User.class);
            query.from(User.class);
            List<User> data = session.createQuery(query).getResultList();
            return data;
        }

        private void createUser() throws Exception
        {
            Customer new_customer = class_message.newCustomer;
            List<User> users = getUsersList();

            for (User user : users)
            {
                if (user.getId().equals(new_customer.getId()) && user.getEmail() == new_customer.getEmail())
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

