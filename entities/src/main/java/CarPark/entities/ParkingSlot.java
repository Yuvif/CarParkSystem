package CarPark.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "parkingslots")
public class ParkingSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String generatedValue;

    private Status SpotStatus;


    @ManyToOne
    @JoinColumn(name = "parkingLotId")
    private Parkinglot parkinglot;

    @OneToOne
    @JoinColumn(name = "checkedInId", nullable = true)
    private CheckedIn checkedIn;

    public CheckedIn getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(CheckedIn checkedIn) {
        this.checkedIn = checkedIn;
    }

    public ParkingSlot() {
    }

    public ParkingSlot(String parkingSlotId,Parkinglot parkinglot) {
        this.generatedValue = parkingSlotId;
        setParkinglot(parkinglot);
        SpotStatus = Status.EMPTY;
    }


    public String getId() {
        return generatedValue;
    }

    public Status getSpotStatus() {
        return SpotStatus;
    }

    public void setSpotStatus(Status spotStatus) {
        SpotStatus = spotStatus;
    }

    public void setId(String id) {
        this.generatedValue = id;
    }

    public Parkinglot getParkinglot() {
        return parkinglot;
    }

    public void setParkinglot(Parkinglot parkinglot) {
        this.parkinglot = parkinglot;
    }

    public Boolean getStatus() {
        return SpotStatus == Status.EMPTY;
    }
    public enum Status {EMPTY, USED, RESTRICTED, RESERVED}
}