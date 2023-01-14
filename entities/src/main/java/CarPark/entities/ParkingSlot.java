package CarPark.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "parkingslots")
public class ParkingSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Status SpotStatus = Status.EMPTY;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parkinglot_id")
    private Parkinglot parkinglot;

    @OneToOne
    private CheckedIn checkedIn;

    public CheckedIn getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(CheckedIn checkedIn) {
        this.checkedIn = checkedIn;
    }

    public ParkingSlot() {
    }

    public ParkingSlot(Parkinglot parkinglot) {
        setParkinglot(parkinglot);
    }



    public int getId() {
        return id;
    }

    public Status getSpotStatus() {
        return SpotStatus;
    }

    public void setSpotStatus(Status spotStatus) {
        SpotStatus = spotStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parkinglot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;
        //parkinglot.getParkingSlots().add(this);
    }
    public Boolean getStatus() {
        return SpotStatus==Status.EMPTY;
    }
    public enum Status {EMPTY, USED, RESTRICTED, RESERVED}
}