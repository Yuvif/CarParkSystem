package CarPark.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "CEO")
public class CEO extends Employee {
    private static CEO instance;

    @ManyToOne
    @JoinColumn(name = "parkinglot_id")
    private Parkinglot parkinglot;

    private List<Price> changeRequests = new ArrayList<>();



    public static CEO getInstance() {
        return instance;
    }
    public Parkinglot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;
    }

    public CEO(String employeeId, String firstName, String lastName, String email, String workersRole, String password, byte[] salt) throws Exception {
        super(employeeId, firstName, lastName, email, workersRole, password, salt);
    }

    public CEO() {
    }

    public void addRequest(Price price)
    {
        changeRequests.add(price);
    }

    public List<Price> getChangeRequests()
    {
        return changeRequests;
    }
}
