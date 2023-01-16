package CarPark.entities;
import javax.persistence.*;

@Entity
@Table(name = "Parking Lot Workers")

public class ParkingLotWorker extends Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Parkinglot parkingLot;

    public ParkingLotWorker(int employeeId, String firstName, String lastName, String email, String workersRole, Parkinglot parkingLot) {
        super(employeeId, firstName, lastName, email, workersRole);
        this.parkingLot = parkingLot;
    }

    public ParkingLotWorker() {}

    public Parkinglot getParkingLot() {
        return parkingLot;
    }
    public void setParkingLot(Parkinglot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public int getId()
    {
        return id;
    }
}