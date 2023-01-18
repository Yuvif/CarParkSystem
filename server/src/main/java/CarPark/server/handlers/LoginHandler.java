package CarPark.server.handlers;
import CarPark.entities.Employee;
import CarPark.entities.User;
import CarPark.entities.messages.LoginMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.query.Query;



public class LoginHandler extends MessageHandler {
    private final LoginMessage class_message;

    public LoginHandler(LoginMessage message, Session session, ConnectionToClient client) {
        super(message, session, client);
        this.class_message = message;
    }

    @Override
    public void handleMessage() throws Exception {
        //generateEmployees();
        CarPark.entities.User user = null;
        String hql = "FROM User WHERE userId = :user_id AND password = :pass";
        Query query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        query.setParameter("pass", class_message.getPassword());
        user = (User) query.uniqueResult();
        if (user!=null) {
            if (user.isLogged())
                class_message.response_type = LoginMessage.ResponseType.ALREADY_LOGGED;
            else {
                class_message.setUser(user);
                class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED;
                user.setLogged(true);
            }
        }
        else {
                class_message.response_type = LoginMessage.ResponseType.LOGIN_FAILED;
        }
    }

        private void generateEmployees() throws Exception {
            Employee employee1 = new Employee(318172848,"Daniel","Glazman","glazman.daniel@gmail.com","ParkingLotWorker","1234567");
            session.save(employee1);
            session.flush();
            Employee employee2 = new Employee(313598484,"Yuval","Fisher","fisheryuval96@gmail.com","CEO", "7777777");
            session.save(employee2);
            session.flush();
        }

    }


