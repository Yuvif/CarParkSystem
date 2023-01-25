package CarPark.entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Parking Lot Managers")
public class ParkingLotManager extends ParkingLotWorker implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    Parkinglot parkinglot;

    public ParkingLotManager(Parkinglot parkinglot)
    {
        this.parkinglot = parkinglot;
    }

    public ParkingLotManager() { }

    public Parkinglot getParkingLot() { return parkinglot; }
    public void setParkinglot(Parkinglot parkinglot) { this.parkinglot = parkinglot; }

    public int getId() { return id; }
}
