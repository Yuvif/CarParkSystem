package CarPark.entities;

import javax.persistence.*;

@Entity
@Table(name = "Parking Lot Workers")

public class ParkingLotWorker extends Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int parkingLotNumber;


    public ParkingLotWorker(int employeeId, String firstName, String lastName, String email, String workersRole, int parkingLotNumber, String password) {
        super(employeeId, firstName, lastName, email, workersRole, password);
        this.parkingLotNumber = parkingLotNumber;
    }

    public ParkingLotWorker() {
    }

    public int getParkingLotNumber() {
        return parkingLotNumber;
    }

    public void setParkingLotNumber(int parkingLotNumber) {
        this.parkingLotNumber = parkingLotNumber;
    }

    public int getId() {
        return id;
    }
}