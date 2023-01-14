package CarPark.server.handlers;
import CarPark.entities.Employee;
import CarPark.server.password.CipherKey;
import CarPark.entities.User;
import CarPark.entities.messages.LoginMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;



public class LoginHandler extends MessageHandler {
    private final LoginMessage class_message;
    private final CipherKey key;

    public LoginHandler(LoginMessage message, Session session, ConnectionToClient client, CipherKey key) {
        super(message, session, client);
        this.class_message = (LoginMessage) this.message;
        this.key = key;
    }

    @Override
    public void handleMessage() throws Exception {
        generateEmployees();
        User user = null;
        String hql = "FROM User WHERE userId = :user_id";
        Query query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        user = (User) query.uniqueResult();
        if (key.decrypt(user.getPassword()).equals(class_message.getPassword())) {
            class_message.setUser(user);
            class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED;
            user.setLogged(true);
        }
        else {
                class_message.response_type = LoginMessage.ResponseType.LOGIN_FAILED;
        }
    }

        private void generateEmployees() throws Exception {
            Employee employee1 = new Employee(318172848,"Daniel","Glazman","glazman.daniel@gmail.com","ParkingLotWorker",key.encrypt("1234567"),
                    false);
            session.save(employee1);
            session.flush();
            Employee employee2 = new Employee(313598484,"Yuval","Fisher","fisheryuval96@gmail.com","CEO", key.encrypt("7777777"),false);
            session.save(employee2);
            session.flush();
        }

    }


