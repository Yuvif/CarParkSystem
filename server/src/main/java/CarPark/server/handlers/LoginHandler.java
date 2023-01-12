package CarPark.server.handlers;
import CarPark.entities.Customer;
import CarPark.entities.Employee;
import CarPark.entities.messages.LoginMessage;
import CarPark.server.ocsf.ConnectionToClient;
import org.hibernate.Session;

import javax.persistence.Query;
import java.security.NoSuchAlgorithmException;


public class LoginHandler extends MessageHandler {
    private final LoginMessage class_message;

    public LoginHandler(LoginMessage message, Session session, ConnectionToClient client) {
        super(message, session, client);
        this.class_message = (LoginMessage) this.message;
    }

    @Override
    public void handleMessage() throws NoSuchAlgorithmException {
        Employee employee = null;
        Customer customer = null;
        String hql = "SELECT DISTINCT Employee"+" FROM Employees" + " WHERE password = :pass AND employeeId = :user_id";
        Query query = session.createQuery(hql);
        query.setParameter("pass", class_message.getPassword());
        query.setParameter("user_id", class_message.getUserId());
        employee = (Employee) query.getSingleResult();
        if (employee != null) {
            class_message.employee = employee;
            class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_EMPLOYEE;
        } else {
            hql = "SELECT * FROM Customers WHERE password = :pass AND customerId = :user_id";
            query = session.createQuery(hql);
            query.setParameter("pass", class_message.getPassword());
            query.setParameter("user_id", class_message.getUserId());
            customer = (Customer) query.getSingleResult();
            if (customer != null) {
                class_message.customer = customer;
                class_message.response_type = LoginMessage.ResponseType.LOGIN_SUCCEED_CUSTOMER;
            } else {
                class_message.response_type = LoginMessage.ResponseType.LOGIN_FAILED;
            }
        }
    }

        private void generateEmployees() throws NoSuchAlgorithmException {
            System.out.println("good");
            Employee employee1 = new Employee(318172848,"Daniel","Glazman","glazman.daniel@gmail.com","ParkingLotWorker","1234567",false);
            session.save(employee1);
            session.flush();
            Employee employee2 = new Employee(313598484,"Yuval","Fisher","fisheryuval96@gmail.com","CEO","7777777",false);
            session.save(employee2);
            session.flush();
        }


    }


