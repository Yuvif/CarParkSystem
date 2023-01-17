package CarPark.entities;

import javax.persistence.*;

@Entity
@Table(name = "Parking Lot Workers")
public class ParkingLotWorker extends Employee {

    private int parkingLotNumber;


    public ParkingLotWorker(int employeeId, String password, String email, String firstName, String lastName, String workersRole, int parkingLotNumber,
                            String pass) throws Exception {
        super(employeeId,password,email,firstName, lastName, workersRole);
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
}