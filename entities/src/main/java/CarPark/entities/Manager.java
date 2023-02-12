package CarPark.entities;

import javax.persistence.*;


@Entity
@AttributeOverrides({
        @AttributeOverride(name = "userId", column = @Column(name = "managerId"))
})
@Table(name = "Managers")
public class Manager extends ParkingLotWorker{

    public Manager(String employeeId, String firstName, String lastName, String email, String workersRole, String password, byte[] salt) throws Exception
    {
        super(employeeId, firstName, lastName, email, workersRole, password, salt);
    }

    public Manager() { }

}
