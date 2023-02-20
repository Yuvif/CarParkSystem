package CarPark.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Manager")
public class Manager extends ParkingLotWorker{

    public Manager(String employeeId, String firstName, String lastName, String email, String workersRole, String
            password,byte[] salt) throws Exception {
        super(employeeId, firstName, lastName, email, workersRole,password,salt);
    }

    public Manager(){}
}
