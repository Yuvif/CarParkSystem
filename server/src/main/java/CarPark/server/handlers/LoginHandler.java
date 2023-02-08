package CarPark.server.handlers;
import CarPark.entities.Customer;
import CarPark.entities.Employee;
import CarPark.entities.messages.LoginMessage;
import CarPark.entities.HashPipeline;
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
    public void handleMessage() throws Exception{
        switch (class_message.request_type) {
            case LOGIN -> login();
            case LOGOUT_NORMALLY, LOGOUT_BY_TERMINATION -> logout();
        }

    }

    public void login() throws Exception {
        //generateEmployees();
        Customer customer;
        String hql = "FROM Customer WHERE userId = :user_id";
        Query query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        customer = (Customer) query.uniqueResult();
        String userPass = null;
        if(customer != null){
            userPass = HashPipeline.toHexString(HashPipeline.getSHA(class_message.getPassword(),customer.getSalt()));
            if(!customer.getPassword().equals(userPass)) {
                customer = null;
            }
        }
        Employee employee;
        hql = "FROM Employee WHERE userId = :user_id";
        query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        employee = (Employee) query.uniqueResult();
        if(employee != null){
            userPass = HashPipeline.toHexString(HashPipeline.getSHA(class_message.getPassword(),employee.getSalt()));
            if(!employee.getPassword().equals(userPass))
                employee = null;
        }
        if (customer != null) {
            if (customer.isLogged())
                class_message.response_type = LoginMessage.ResponseType.ALREADY_LOGGED;
            else {
                class_message.setUser(customer);
                class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_CUSTOMER;
                customer.setLogged(true);
            }
        } else {
            if (employee != null) {
                if (employee.isLogged())
                    class_message.response_type = LoginMessage.ResponseType.ALREADY_LOGGED;
                else {
                    class_message.setUser(employee);
                    class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_EMPLOYEE;
                    employee.setLogged(true);
                }
            } else {
                class_message.response_type = LoginMessage.ResponseType.LOGIN_FAILED;
            }
        }
    }

    public void logout()
    {
        Customer customer;
        String hql = "FROM Customer WHERE userId = :user_id";
        Query query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        customer = (Customer) query.uniqueResult();
        Employee employee;
        hql = "FROM Employee WHERE userId = :user_id";
        query = session.createQuery(hql);
        query.setParameter("user_id", class_message.getUserId());
        employee = (Employee) query.uniqueResult();
        if (employee!=null)
            employee.setLogged(false);
        else
            customer.setLogged(false);
    }

//        private void generateEmployees() throws Exception {
//            Employee employee1 = new Employee(318172848,"Daniel","Glazman","glazman.daniel@gmail.com","ParkingLotWorker","1234567");
//            session.save(employee1);
//            session.flush();
//            Employee employee2 = new Employee(313598484,"Yuval","Fisher","fisheryuval96@gmail.com","CEO", "7777777");
//            session.save(employee2);
//            session.flush();
//        }

    }


