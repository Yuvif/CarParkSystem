package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "parkingslots")
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Status SpotStatus = Status.EMPTY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parkinglot_id")
    private Parkinglot parkinglot;


    public ParkingSlot() {
    }

    public ParkingSlot(Parkinglot parkinglot) {

        setParkinglot(parkinglot);


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parkinglot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;

        parkinglot.getParkingSlots().add(this);
    }
    public enum Status {EMPTY, USED, RESTRICTED}
}