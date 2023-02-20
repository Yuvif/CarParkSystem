package CarPark.entities;
import javax.persistence.*;

@Entity
@Table(name = "ParkingLotWorkers")
public class ParkingLotWorker extends Employee {


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parkinglotId")
    private Parkinglot parkinglot;


    public ParkingLotWorker(String employeeId, String firstName, String lastName, String email, String workersRole, String password, byte[] salt) throws Exception {
        super(employeeId, firstName, lastName, email, workersRole, password, salt);
    }

    public ParkingLotWorker() {}

    public Parkinglot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;
    }
    public Parkinglot getParkingLot() {
        return parkinglot;
    }
    public void setParkingLot(Parkinglot parkingLot) {
        this.parkinglot = parkingLot;
    }

}